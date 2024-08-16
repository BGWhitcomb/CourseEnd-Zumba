package com.courseend.zumba.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.courseend.zumba.database.Database;
import com.courseend.zumba.model.Batch;

@WebServlet("/add-batch")
public class BatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String scheduledOnStr = request.getParameter("scheduledOn");
		String startTime = request.getParameter("startTime");
		StringBuilder errorMessage = new StringBuilder();

		validateFormData(name, scheduledOnStr, startTime, errorMessage);

		if (errorMessage.length() > 0) {
			request.setAttribute("errorMessage", errorMessage.toString());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/add-batch.jsp");
			dispatcher.forward(request, response);
			return;
		}

		LocalDate scheduledOn = LocalDate.parse(scheduledOnStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Batch batch = new Batch();
		batch.setName(name);
		batch.setScheduledOn(scheduledOn);
		batch.setStartTime(startTime);

		Database db = Database.getInstance();
		String insertBatchSql = "INSERT INTO Batch (name, scheduledOn, startTime) VALUES (?, ?, ?)";

		try (Connection connection = db.getConnection();
				PreparedStatement ps = connection.prepareStatement(insertBatchSql)) {

			ps.setString(1, batch.getName());
			ps.setDate(2, java.sql.Date.valueOf(batch.getScheduledOn()));
			ps.setString(3, batch.getStartTime());

			int result = ps.executeUpdate();
			if (result > 0) {
				request.setAttribute("successMessage", "Batch added successfully!");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/add-batch.jsp");
				dispatcher.forward(request, response);
			} else {
				request.setAttribute("errorMessage", "Failed to add batch.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/add-batch.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Database error: " + e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/add-batch.jsp");
			dispatcher.forward(request, response);
		} finally {
			db.closeConnection();
		}
	}

	private void validateFormData(String name, String scheduledOn, String startTime, StringBuilder errorMessage) {
		if (name == null || name.trim().isEmpty()) {
			errorMessage.append("Name is required. ");
		}
		if (scheduledOn == null || scheduledOn.trim().isEmpty()) {
			errorMessage.append("Scheduled date is required. ");
		}
		if (startTime == null || startTime.trim().isEmpty()) {
			errorMessage.append("Start time is required. ");
		}
	}
}
