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

@WebServlet("/add-participant")
public class ParticipantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//logic to populate dropdown in add-participant form to select class
		Database db = Database.getInstance();
		String selectBatchSql = "SELECT bid, name, scheduledOn, startTime " +
                "FROM Batch " +
                "ORDER BY scheduledOn ASC, startTime ASC";
		List<Batch> batches = new ArrayList<>();

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(selectBatchSql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Batch batch = new Batch();
				batch.setBid(rs.getInt("bid"));
				batch.setName(rs.getString("name"));

				// Check if the date is null before converting
				java.sql.Date sqlDate = rs.getDate("scheduledOn");
				if (sqlDate != null) {
					batch.setScheduledOn(sqlDate.toLocalDate());
				} else {
					batch.setScheduledOn(null);
				}

				batch.setStartTime(rs.getString("startTime"));
				batches.add(batch);
			}

			if ("true".equals(request.getParameter("ajax"))) {
				// Handle AJAX request
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.print("[");
				for (int i = 0; i < batches.size(); i++) {
					Batch batch = batches.get(i);
					out.printf("{\"bid\": %d, \"name\": \"%s\", \"scheduledOn\": \"%s\", \"startTime\": \"%s\"}",
							batch.getBid(), batch.getName(),
							(batch.getScheduledOn() != null ? batch.getScheduledOn().toString() : "null"),
							batch.getStartTime());
					if (i < batches.size() - 1) {
						out.print(",");
					}
				}
				out.print("]");
			} else {
				request.setAttribute("batches", batches);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/add-participant.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("Error retrieving batches", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String bidParam = request.getParameter("bid");

		StringBuilder errorMessage = new StringBuilder();

		validateFormData(name, phone, email, bidParam, errorMessage);

		if (errorMessage.length() > 0) {
			forwardToJsp(request, response, "/add-participant.jsp", errorMessage.toString().trim());
			return;
		}

		int bid;
		try {
			bid = Integer.parseInt(bidParam);
		} catch (NumberFormatException e) {
			forwardToJsp(request, response, "/add-participant.jsp", "Invalid batch ID format.");
			return;
		}

		Participant participant = new Participant();
		participant.setName(name);
		participant.setPhone(phone);
		participant.setEmail(email);
		participant.setBid(bid);

		Database db = Database.getInstance();
		String insertParticipantSql = "INSERT INTO Participant (name, phone, email, bid) VALUES (?, ?, ?, ?)";

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(insertParticipantSql)) {

			ps.setString(1, participant.getName());
			ps.setString(2, participant.getPhone());
			ps.setString(3, participant.getEmail());
			ps.setInt(4, participant.getBid());

			int result = ps.executeUpdate();
			if (result > 0) {
				request.setAttribute("successMessage", "Participant added successfully!");
				request.setAttribute("participantName", participant.getName());
				request.setAttribute("participantPhone", participant.getPhone());
				request.setAttribute("participantEmail", participant.getEmail());
				request.setAttribute("participantBID", participant.getBid());

				forwardToJsp(request, response, "/add-participant.jsp", null);
			} else {
				forwardToJsp(request, response, "errorMessage", "Failed to add participant.");
				forwardToJsp(request, response, "/add-participant.jsp", null);
			}
		} catch (SQLException e) {
			forwardToJsp(request, response, "/add-participant.jsp", "Database error: " + e.getMessage());
		} finally {
			db.closeConnection();
		}
	}

	private void validateFormData(String name, String phone, String email, String bidParam,
			StringBuilder errorMessage) {
		if (name == null || name.trim().isEmpty()) {
			errorMessage.append("Name is required. ");
		}
		if (phone == null || phone.trim().isEmpty()) {
			errorMessage.append("Phone is required. ");
		}
		if (email == null || email.trim().isEmpty()) {
			errorMessage.append("Email is required. ");
		} else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			errorMessage.append("Invalid email format. ");
		}
		if (bidParam == null || bidParam.isEmpty()) {
			errorMessage.append("Please select a class. ");
		}
	}

	private void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspPath,
			String errorMessage) throws ServletException, IOException {
		if (errorMessage != null) {
			request.setAttribute("errorMessage", errorMessage);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
		dispatcher.forward(request, response);
	}

}
