package com.courseend.zumba.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.courseend.zumba.model.Participant;

@WebServlet("/update-participant")
public class ParticipantUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if ("getClasses".equals(action)) {
			// Retrieve all batches for the edit dropdown
			getBatchDropDown(response);
		} else {
			// Retrieve participants and their assigned batches for the table
			getParticipants(request, response);
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
		}
	}

	private void getBatchDropDown(HttpServletResponse response) throws IOException {
		Database db = Database.getInstance();
		String selectAllBatchesSql = "SELECT bid, name, scheduledOn, startTime " + "FROM Batch "
				+ "ORDER BY scheduledOn ASC, startTime ASC";
		List<Batch> batches = new ArrayList<>();

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(selectAllBatchesSql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Batch batch = new Batch();
				batch.setBid(rs.getInt("bid"));
				batch.setName(rs.getString("name"));
				batch.setScheduledOn(
						rs.getDate("scheduledOn") != null ? rs.getDate("scheduledOn").toLocalDate() : null);
				batch.setStartTime(rs.getString("startTime"));
				batches.add(batch);
			}

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print("[");
			for (int i = 0; i < batches.size(); i++) {
				Batch batch = batches.get(i);
				out.printf("{\"bid\": %d, \"name\": \"%s\", \"scheduledOn\": \"%s\", \"startTime\": \"%s\"}",
						batch.getBid(), batch.getName(), batch.getScheduledOn(), batch.getStartTime());
				if (i < batches.size() - 1) {
					out.print(",");
				}
			}
			out.print("]");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
		}
	}

	private void getParticipants(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Database db = Database.getInstance();

		// SQL query to retrieve participant and related batch data
		String selectParticipantsSql = "SELECT p.pid, p.name, p.phone, p.email, p.bid, "
				+ "b.name AS batchName, b.scheduledOn, b.startTime " + "FROM Participant p "
				+ "INNER JOIN Batch b ON p.bid = b.bid " + "ORDER BY b.scheduledOn ASC, b.startTime ASC";

		List<Participant> participants = new ArrayList<>();

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(selectParticipantsSql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Participant participant = new Participant();
				participant.setPid(rs.getInt("pid"));
				participant.setName(rs.getString("name"));
				participant.setPhone(rs.getString("phone"));
				participant.setEmail(rs.getString("email"));
				participant.setBid(rs.getInt("bid"));
				participant.setBatchName(rs.getString("batchName"));
				participant.setScheduledOn(rs.getDate("scheduledOn").toLocalDate());
				participant.setStartTime(rs.getString("startTime"));
				participants.add(participant);
			}

			if ("true".equals(request.getParameter("ajax"))) {
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print("[");
				for (int i = 0; i < participants.size(); i++) {
					Participant participant = participants.get(i);
					out.printf("{\"pid\": %d, \"name\": \"%s\", \"phone\": \"%s\", \"email\": \"%s\", "
							+ "\"batchName\": \"%s\", \"scheduledOn\": \"%s\", \"startTime\": \"%s\", \"bid\": %d}",
							participant.getPid(), participant.getName(), participant.getPhone(), participant.getEmail(),
							participant.getBatchName(), participant.getScheduledOn(), participant.getStartTime(),
							participant.getBid());
					if (i < participants.size() - 1) {
						out.print(",");
					}
				}
				out.print("]");
			} else {
				request.setAttribute("participants", participants);

				String success = request.getParameter("success");
				if ("true".equals(success)) {
					request.setAttribute("successMessage", "Participant successfully updated!");
				}

				RequestDispatcher dispatcher = request.getRequestDispatcher("/update-participant.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if ("true".equals(request.getParameter("ajax"))) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
			} else {
				request.setAttribute("errorMessage", "Database error.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/update-participant.jsp");
				dispatcher.forward(request, response);
			}
		}
	}

	private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		int bid = Integer.parseInt(request.getParameter("bid"));

		Database db = Database.getInstance();

		String updateParticipantSql = "UPDATE Participant SET name = ?, phone = ?, email = ?, bid = ? WHERE pid = ?";

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(updateParticipantSql)) {

			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setString(3, email);
			ps.setInt(4, bid);
			ps.setInt(5, pid);

			int result = ps.executeUpdate();
			if (result > 0) {
				request.setAttribute("successMessage", "Participant successfully updated!");
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update participant.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
		}
	}

	private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));

		Database db = Database.getInstance();
		String deleteParticipantSql = "DELETE FROM Participant WHERE pid = ?";

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(deleteParticipantSql)) {

			ps.setInt(1, pid);

			int result = ps.executeUpdate();
			if (result > 0) {
				response.sendRedirect(request.getContextPath() + "/update-participant");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete participant.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL Error.");
		}
	}
}
