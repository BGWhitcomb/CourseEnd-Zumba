<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Add Batch</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles.css">
</head>
<body class="body">
	<!-- Bootstrap Navbar -->
	<nav class="navbar navbar-expand-lg navbar-light shadow-sm">
		<div class="container-fluid">
			<a class="navbar-brand fw-bold" href="index.jsp">Zumba Gym</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/index.jsp">Home</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/add-participant.jsp">Add
							Participant</a></li>
					<li class="nav-item"><a class="nav-link active"
						href="<%=request.getContextPath()%>/add-batch.jsp">Add Batch</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/update-participant.jsp">View
							Participants</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/update-batch.jsp">View
							Batches</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container form-card mt-5 p-4 rounded">
		<!-- Error message display -->
		<c:if test="${not empty errorMessage}">
			<div class="alert alert-danger">${errorMessage}</div>
		</c:if>

		<c:if test="${not empty successMessage}">
			<div class="alert alert-success">${successMessage}</div>
		</c:if>

		<h2 class="card-title text-center">Add New Batch</h2>
		<form action="${pageContext.request.contextPath}/add-batch"
			method="post">
			<div class="mb-3">
				<label for="name" class="form-label">Name</label> <input type="text"
					id="name" name="name" class="form-control" placeholder="Batch Name"
					required>
			</div>
			<div class="mb-3">
				<label for="scheduledOn" class="form-label">Scheduled On</label> <input
					type="date" id="scheduledOn" name="scheduledOn"
					class="form-control" required>
			</div>
			<div class="mb-3">
				<label for="startTime" class="form-label">Start Time</label> <input
					type="time" id="startTime" name="startTime" class="form-control"
					required>
			</div>
			<div class="text-center">
				<button type="submit" class="btn btn-primary btn-lg">Add
					Batch</button>
			</div>
		</form>
	</div>

	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
