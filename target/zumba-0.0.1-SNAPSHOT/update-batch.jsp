<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.util.List"%>
<%@ page import="com.courseend.zumba.model.Batch"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Batches</title>
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
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/add-batch.jsp">Add Batch</a></li>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/update-participant.jsp">View
							Participants</a></li>
					<li class="nav-item"><a class="nav-link active"
						href="<%=request.getContextPath()%>/update-batch.jsp">View
							Batches</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="mt-5">
		<h2 class="mb-4 text-center">Batch List</h2>


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
					<th>Scheduled On</th>
					<th>Start Time</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="batchTableBody">
				<tr>
					<td colspan="4">Loading...</td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- Modal for Edit Batch -->
	<div class="modal fade" id="editBatchModal" tabindex="-1"
		aria-labelledby="editBatchModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="editBatchModalLabel">Edit Batch</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<form id="editBatchForm">
						<label for="bid" class="form-label">ID</label><input type="text"
							id="bid" name="bid" class="form-control" readonly>
						<div class="mb-3">
							<label for="name" class="form-label">Name</label> <input
								type="text" class="form-control" id="name" name="name" required>
						</div>
						<div class="mb-3">
							<label for="scheduledOn" class="form-label">Scheduled On</label>
							<input type="date" class="form-control" id="scheduledOn"
								name="scheduledOn" required>
						</div>
						<div class="mb-3">
							<label for="startTime" class="form-label">Start Time</label> <input
								type="time" class="form-control" id="startTime" name="startTime"
								required>
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
	</div>

	<!-- Participant List Modal -->
	<div class="modal fade" id="participantListModal" tabindex="-1"
		aria-labelledby="participantListModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="participantListModalLabel">Participants</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<ul id="participantList" class="list-group">
						<!-- Participant names will be dynamically inserted here -->
					</ul>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>


	<!-- Delete Confirmation Modal -->
	<div class="modal fade" id="deleteBatchModal" tabindex="-1"
		aria-labelledby="deleteBatchModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="deleteBatchModalLabel">Confirm
						Delete</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">Are you sure you want to delete this
					batch? This action cannot be undone.</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-danger" id="confirmDeleteBtn">Delete</button>
				</div>
			</div>
		</div>
	</div>



	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>

<script>
$(document).ready(function() {
    // Variable to store the batch ID
    let batchIdToDelete;

    // Load batches on page load
    getBatches();
   

    function getBatches() {
        $.ajax({
            url: '<%=request.getContextPath()%>/update-batch',
            type: 'GET',
            data: { ajax: 'true', action: 'getBatches' },
            dataType: 'json',
            success: function(data) {
                const tbody = $('#batchTableBody');
                tbody.empty();
                if (data.length > 0) {
                    data.forEach(function(batch) {
                        var date = new Date(batch.scheduledOn + 'T00:00:00');
                        var formattedDate = date.toLocaleDateString();
                        var formattedStartTime = formatTime(batch.startTime);
                        var row = $('<tr>')
                            .append('<td>' + batch.name + '</td>')
                            .append('<td>' + formattedDate + '</td>')
                            .append('<td>' + formattedStartTime + '</td>')
                            .append('<td>' +
                                '<div style="display: flex; justify-content: center; gap: 5px;">' +
                                    '<button class="btn btn-outline-info btn-sm" onclick="editBatch(' + batch.bid + ', \'' + batch.name + '\', \'' + batch.scheduledOn + '\', \'' + batch.startTime + '\')">Edit</button>' +
                                    '<button class="btn btn-outline-danger btn-sm" onclick="showDeleteModal(' + batch.bid + ')">Delete</button>' +
                                    '<button class="btn btn-outline-primary btn-sm" onclick="getParticipantsByBatch(' + batch.bid + ')">Participants</button>' +
                                '</div>' +
                            '</td>');
                        tbody.append(row);
                    });
                } else {
                    tbody.append($('<tr>').append($('<td colspan="4">No Batches found.</td>')));
                }
            },
            error: function() {
                console.error('Failed to load batches.');
            }
        });
    }

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
    
    window.getParticipantsByBatch = function(bid) {
        $.ajax({
            url: '<%=request.getContextPath()%>/update-batch',
            type: 'GET',
            data: { action: 'getParticipantsByBatch', bid: bid },
            dataType: 'json',
            success: function(data) {
                var participantList = $('#participantList');
                participantList.empty();
                if (data.length > 0) {
                    data.forEach(function(name) {
                        participantList.append('<li class="list-group-item-dark">' + name + '</li>');
                    });
                } else {
                    participantList.append('<li class="list-group-item-dark">No participants found.</li>');
                }
                $('#participantListModal').modal('show');
            },
            error: function() {
                alert('Failed to load participants.');
            }
        });
    }

    window.editBatch = function(bid, name, scheduledOn, startTime) {
        $('#bid').val(bid);
        $('#name').val(name);
        $('#scheduledOn').val(scheduledOn);
        $('#startTime').val(startTime);
        $('#editBatchModal').modal('show');
    };

    $('#editBatchForm').on('submit', function(event) {
        event.preventDefault();
        const formData = $(this).serialize() + '&action=update';
        $.ajax({
            url: '<%=request.getContextPath()%>/update-batch',
            type: 'POST',
            data: formData,
            success: function() {
                $('#editBatchModal').modal('hide');
                getBatches();
            },
            error: function() {
                alert('Failed to update Batch.');
            }
        });
    });

    window.showDeleteModal = function(bid) {
        batchIdToDelete = bid;
        $('#deleteBatchModal').modal('show');
    };

    $('#confirmDeleteBtn').on('click', function() {
        $.ajax({
            url: '<%=request.getContextPath()%>/update-batch',
            type: 'POST',
            data: {
                action: 'delete',
                bid: batchIdToDelete
            },
            success: function() {
                $('#deleteBatchModal').modal('hide');
                getBatches();
            },
            error: function() {
                alert('Failed to delete batch. It might have participants assigned to it.');
            }
        });
    });
});
</script>






</body>
</html>
