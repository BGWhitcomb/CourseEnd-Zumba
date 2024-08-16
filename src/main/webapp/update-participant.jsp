<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Participants</title>
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
                        href="<%=request.getContextPath()%>/add-participant.jsp">Add Participant</a></li>
                    <li class="nav-item"><a class="nav-link"
                        href="<%=request.getContextPath()%>/add-batch.jsp">Add Batch</a></li>
                    <li class="nav-item"><a class="nav-link active"
                        href="<%=request.getContextPath()%>/update-participant.jsp">View Participants</a></li>
                    <li class="nav-item"><a class="nav-link"
                        href="<%=request.getContextPath()%>/update-batch.jsp">View Batches</a></li>
                </ul>
            </div>
        </div>
    </nav>
    
    

	<div class="mt-5">
	<h2 class="mb-4 text-center">Participant List</h2>
		<!-- Display success message -->
		<c:if test="${not empty errorMessage}">
			<div class="alert alert-danger">${errorMessage}</div>
		</c:if>
		
		<c:if test="${not empty successMessage}">
			<div class="alert alert-success">${successMessage}</div>
		</c:if>



		<table class="table table-bordered table-striped text-center">
			<thead class="thead-light">
				<tr>
					<th>Name</th>
					<th>Phone</th>
					<th>Email</th>
					<th>Class</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="participantTableBody">
				<tr>
					<td colspan="5">Loading...</td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- Edit Participant Modal -->
	<div class="modal fade" id="editParticipantModal" tabindex="-1"
		aria-labelledby="editParticipantModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="editParticipantModalLabel">Edit
						Participant</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form id="editParticipantForm">
					<div class="modal-body">
						<div class="mb-3">
							<label for="pid" class="form-label">ID</label> <input type="text"
								class="form-control" id="pid" name="pid" readonly>
						</div>
						<div class="mb-3">
							<label for="name" class="form-label">Name</label> <input
								type="text" class="form-control" id="name" name="name">
						</div>
						<div class="mb-3">
							<label for="phone" class="form-label">Phone</label> <input
								type="text" class="form-control" id="phone" name="phone">
						</div>
						<div class="mb-3">
							<label for="email" class="form-label">Email</label> <input
								type="email" class="form-control" id="email" name="email">
						</div>
						<div class="mb-3">
							<label for="bid" class="form-label">Class</label> <select
								class="form-control" id="bid" name="bid">
								<!-- Options will be dynamically populated here -->
							</select>

						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Close</button>
						<button type="submit" class="btn btn-primary">Save
							changes</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<!-- Delete Confirmation Modal -->
	<div class="modal fade" id="deleteParticipantModal" tabindex="-1"
		aria-labelledby="deleteParticipantModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="deleteParticipantModalLabel">Confirm
						Delete</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">Are you sure you want to delete this
					participant? This action cannot be undone.</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-danger" id="confirmDeleteBtn">Delete</button>
				</div>
			</div>
		</div>
	</div>

	<!-- JavaScript -->
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>

	<script>
$(document).ready(function() {
    let participantIdToDelete;

    getClasses().then(getParticipants).catch(function(error) {
        console.error('Failed to load classes:', error);
    });

    function formatTime(timeString) {
        const time = new Date('1970-01-01T' + timeString);
        let hours = time.getHours();
        const minutes = time.getMinutes();
        const amPm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12;
        return hours.toString().padStart(2, '0') + ':' + minutes.toString().padStart(2, '0') + ' ' + amPm;
    }

    function getClasses() {
        return new Promise(function(resolve, reject) {
            $.ajax({
                url: '<%=request.getContextPath()%>/update-participant',
                type: 'GET',
                data: { action: 'getClasses', ajax: 'true' },
                dataType: 'json',
                success: function(data) {
                    const $bidSelect = $('#bid');
                    $bidSelect.empty();
                    data.forEach(function(batch) {
                        const date = new Date(batch.scheduledOn + 'T00:00:00');
                        const formattedDate = date.toLocaleDateString();
                        const formattedStartTime = formatTime(batch.startTime);
                        $bidSelect.append(
                            '<option value="' + batch.bid + '">' + batch.name + ' - ' + formattedDate + ' at ' + formattedStartTime + '</option>'
                        );
                    });
                    resolve();
                },
                error: function() {
                    reject('Failed to load classes.');
                }
            });
        });
    }

    function getParticipants() {
        $.ajax({
            url: '<%=request.getContextPath()%>/update-participant',
            type: 'GET',
            dataType: 'json',
            data: { ajax: 'true' },
            success: function(data) {
                const tbody = $('#participantTableBody');
                tbody.empty();
                if (data.length > 0) {
                    data.forEach(function(participant) {
                        const date = new Date(participant.scheduledOn + 'T00:00:00');
                        const formattedDate = date.toLocaleDateString();
                        const formattedStartTime = formatTime(participant.startTime);
                        const row = $('<tr>')
                            .append('<td>' + participant.name + '</td>')
                            .append('<td>' + participant.phone + '</td>')
                            .append('<td>' + participant.email + '</td>')
                            .append('<td><strong>' + participant.batchName + '</strong> - <strong>' + formattedDate + '</strong> at <strong>' + formattedStartTime + '</strong></td>')
                            .append(
                                '<td>' +
                                '<div class="d-flex justify-content-center">' +
                                    '<button class="btn btn-outline-info btn-sm me-2" onclick="editParticipant(' + participant.pid + ', \'' + participant.name + '\', \'' + participant.phone + '\', \'' + participant.email + '\', ' + participant.bid + ')">Edit</button>' +
                                    '<button class="btn btn-outline-danger btn-sm" onclick="showDeleteModal(' + participant.pid + ')">Delete</button>' +
                                '</div>' +
                                '</td>'
                            );
                        tbody.append(row);
                    });
                } else {
                    tbody.append($('<tr>').append($('<td colspan="5">No participants found.</td>')));
                }
            },
            error: function() {
                console.error('Failed to load participants.');
            }
        });
    }

    $('#editParticipantForm').on('submit', function(event) {
        event.preventDefault();
        const formData = $(this).serialize() + '&action=update';
        $.ajax({
            url: '<%=request.getContextPath()%>/update-participant',
            type: 'POST',
            data: formData,
            success: function() {
                $('#editParticipantModal').modal('hide');
                getParticipants();
            },
            error: function() {
                alert('Failed to update participant.');
            }
        });
    });

    window.editParticipant = function(pid, name, phone, email, bid) {
        $('#pid').val(pid);
        $('#name').val(name);
        $('#phone').val(phone);
        $('#email').val(email);
        $('#bid').val(bid);
        $('#editParticipantModal').modal('show');
    };

    window.showDeleteModal = function(pid) {
        participantIdToDelete = pid;
        $('#deleteParticipantModal').modal('show');
    };

    $('#confirmDeleteBtn').on('click', function() {
        $.ajax({
            url: '<%=request.getContextPath()%>/update-participant',
            type: 'POST',
            data: {
                action: 'delete',
                pid: participantIdToDelete
            },
            success: function() {
                $('#deleteParticipantModal').modal('hide');
                getParticipants();
            },
            error: function() {
                alert('Failed to delete the participant.');
            }
        });
    });
});
</script>






</body>
</html>
