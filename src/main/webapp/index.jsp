<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Zumba Gym</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles.css">
</head>
<body class="body">
	<!-- Bootstrap Navbar -->
	<nav class="navbar navbar-expand-lg navbar-light shadow-sm">
		<div class="container-fluid">
			<a class="navbar-brand fw-bold">Zumba Gym</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link active"
						href="<%=request.getContextPath()%>/index.jsp">Home</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/add-participant.jsp">Add
							Participant</a></li>
					<li class="nav-item"><a class="nav-link"
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

	<div class="card container mt-5">
		<div class="text-center  p-5">
			<h2 class="mb-4 fw-bold">Zumba Gym Management Solution</h2>
			<p class="mb-4 fs-5">Easily manage your Zumba classes with our
				all-in-one platform. Add participants and batches, view existing
				schedules, and effortlessly edit or update your batches and
				participants. Your Zumba routine, organized and simplified.</p>
			<a href="<%=request.getContextPath()%>/add-participant.jsp"
				class="btn btn-primary btn-lg">Get Started</a>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
