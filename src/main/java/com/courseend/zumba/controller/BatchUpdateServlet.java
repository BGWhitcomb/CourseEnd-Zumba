package com.courseend.zumba.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.courseend.zumba.database.Database;
import com.courseend.zumba.model.Batch;

@WebServlet("/update-batch")
public class BatchUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean isAjax = "true".equals(request.getParameter("ajax"));
		String action = request.getParameter("action");

		if ("getBatches".equals(action)) {
			getBatches(request, response, isAjax);
		} else if ("getParticipantsByBatch".equals(action)) {
			getParticipantsByBatch(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("update".equals(action)) {
			handleUpdate(request, response);
		} else if ("delete".equals(action)) {
			handleDelete(request, response);
		} else {
			// Handle invalid action or forward to a proper error page
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
		}
	}

	private void getBatches(HttpServletRequest request, HttpServletResponse response, boolean isAjax)
			throws IOException, ServletException {

		Database db = Database.getInstance();

		String selectBatchSql = "SELECT bid, name, scheduledOn, startTime " + "FROM Batch "
				+ "ORDER BY scheduledOn ASC, startTime ASC";
		List<Batch> batches = new ArrayList<>();

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(selectBatchSql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Batch batch = new Batch();
				batch.setBid(rs.getInt("bid"));
				batch.setName(rs.getString("name"));

				// Handle possible null values
				java.sql.Date sqlDate = rs.getDate("scheduledOn");
				if (sqlDate != null) {
					batch.setScheduledOn(sqlDate.toLocalDate());
				} else {
					batch.setScheduledOn(null);
				}

				batch.setStartTime(rs.getString("startTime"));
				batches.add(batch);

			}

			if (isAjax) {
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print("[");
				for (int i = 0; i < batches.size(); i++) {
					Batch batch = batches.get(i);
					out.printf("{\"bid\": %d, \"name\": \"%s\", \"scheduledOn\": \"%s\", \"startTime\": \"%s\"}",
							batch.getBid(), batch.getName(),
							batch.getScheduledOn() != null ? batch.getScheduledOn().toString() : "",
							batch.getStartTime());
					if (i < batches.size() - 1) {
						out.print(",");
					}
				}
				out.print("]");
			} else {
				request.setAttribute("batches", batches);

				// success message for successful edit
				String success = request.getParameter("success");
				if ("true".equals(success)) {
					request.setAttribute("successMessage", "Batch updated successfully!");
				}

				RequestDispatcher dispatcher = request.getRequestDispatcher("/update-batch.jsp");
				dispatcher.forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			if (!isAjax) {
				request.setAttribute("errorMessage", "Failed to load batches: " + e.getMessage());
				RequestDispatcher dispatcher = request.getRequestDispatcher("/update-batch.jsp");
				dispatcher.forward(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error: " + e.getMessage());
			}
		} finally {
			db.closeConnection();
		}
	}

	private void getParticipantsByBatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int bid = Integer.parseInt(request.getParameter("bid"));
		Database db = Database.getInstance();
		String selectParticipantsSql = "SELECT name FROM Participant WHERE bid = ?";
		List<String> participants = new ArrayList<>();

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(selectParticipantsSql)) {
			ps.setInt(1, bid);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				participants.add(rs.getString("name"));
			}

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print("[");
			for (int i = 0; i < participants.size(); i++) {
				out.print("\"" + participants.get(i) + "\"");
				if (i < participants.size() - 1) {
					out.print(",");
				}
			}
			out.print("]");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
		}
	}

	private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int bid = Integer.parseInt(request.getParameter("bid"));
		String name = request.getParameter("name");
		LocalDate scheduledOn = null;
		String scheduledOnParam = request.getParameter("scheduledOn");
		if (scheduledOnParam != null && !scheduledOnParam.isEmpty()) {
			scheduledOn = LocalDate.parse(scheduledOnParam);
		}
		String startTime = request.getParameter("startTime");

		Database db = Database.getInstance();
		String updateBatchSql = "UPDATE Batch SET name = ?, scheduledOn = ?, startTime = ? WHERE bid = ?";

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(updateBatchSql)) {

			ps.setString(1, name);
			ps.setDate(2, scheduledOn != null ? Date.valueOf(scheduledOn) : null);
			ps.setString(3, startTime);
			ps.setInt(4, bid);

			int result = ps.executeUpdate();
			if (result > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update batch.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
		} finally {
			db.closeConnection();
		}
	}

	private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int bid = Integer.parseInt(request.getParameter("bid"));
		Database db = Database.getInstance();
		String checkParticipantsSql = "SELECT COUNT(pid) AS participantCount FROM Participant WHERE bid = ?";
		String deleteBatchSql = "DELETE FROM Batch WHERE bid = ?";

		try (Connection connection = db.getConnection();
				PreparedStatement checkStmt = connection.prepareStatement(checkParticipantsSql);
				PreparedStatement deleteStmt = connection.prepareStatement(deleteBatchSql)) {

			checkStmt.setInt(1, bid);
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next() && rs.getInt("participantCount") > 0) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Cannot delete batch with assigned participants.");
				return;
			}

			deleteStmt.setInt(1, bid);
			int result = deleteStmt.executeUpdate();
			if (result > 0) {
				response.sendRedirect(request.getContextPath() + "/update-batch?success=true");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete batch.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
		}

	}

}