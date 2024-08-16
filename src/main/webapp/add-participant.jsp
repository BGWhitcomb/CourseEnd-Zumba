<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Add Participant</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles.css">
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
					<li class="nav-item"><a class="nav-link active"
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

	<div class="container mt-5">
		<div class="form-card p-4 rounded">
			<!-- message display -->
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-danger">${errorMessage}</div>
			</c:if>

			<c:if test="${not empty successMessage}">
				<div class="alert alert-success">${successMessage}</div>
			</c:if>

			<h2 class="card-title text-center">Add New Participant</h2>
			<form action="${pageContext.request.contextPath}/add-participant"
				method="post">
				<div class="mb-3">
					<label for="name" class="form-label">Full Name</label>
					<div>
						<input type="text" id="name" name="name" class="form-control"
							placeholder="Full Name" required>
					</div>
				</div>
				<div class="mb-3">
					<label for="phone" class="form-label">Phone</label>
					<div>
						<input type="text" id="phone" name="phone" class="form-control"
							placeholder="Enter Phone Number" required>
					</div>
				</div>
				<div class="mb-3">
					<label for="email" class="form-label">Email</label>
					<div>
						<input type="email" id="email" name="email" class="form-control"
							placeholder="Enter Email" required>
					</div>
				</div>
				<div class="mb-3">
					<label for="bid" class="form-label">Choose Class</label>
					<div>
						<select id="bid" name="bid" class="form-control form-select"
							required>
							<option value="" selected disabled>Selection Required</option>
						</select>
					</div>
				</div>
				<div class="text-center">
					<button type="submit" class="btn btn-primary btn-lg">Add
						Participant</button>
				</div>
			</form>
		</div>
	</div>


	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script>
    $(document).ready(function() {
        // Function to format time
        function formatTime(timeString) {
            const time = new Date('1970-01-01T' + timeString);
            let hours = time.getHours();
            const minutes = time.getMinutes();
            const amPm = hours >= 12 ? 'PM' : 'AM';
            hours = hours % 12;
            hours = hours ? hours : 12;
            return hours.toString().padStart(2, '0') + ':' +
                   minutes.toString().padStart(2, '0') + ' ' + amPm;
        }

        // Function to populate the dropdown with batch data
        function getBatchData() {
            $.ajax({
                url: '${pageContext.request.contextPath}/add-participant',
                type: 'GET',
                dataType: 'json',
                data: { ajax: 'true' },
                success: function(data) {
                    var $bidSelect = $('#bid');
                    $bidSelect.empty();
                    $bidSelect.append('<option value="" selected disabled>Selection Required</option>');

                    data.forEach(function(batch) {
                        var date = new Date(batch.scheduledOn + 'T00:00:00');
                        var formattedDate = date.toLocaleDateString();
                        var formattedStartTime = formatTime(batch.startTime);

                        $bidSelect.append($('<option>', {
                            value: batch.bid,
                            text: batch.name + ' - ' + formattedDate + ' at ' + formattedStartTime
                        }));
                    });
                },
                error: function() {
                    console.error('Failed to load batch data.');
                }
            });
        }

        // Call to populate dropdown when the page loads
        getBatchData();
    });
</script>



</body>
</html>
