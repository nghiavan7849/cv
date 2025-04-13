
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<div class="container-fluid">
				<div class="row mb-2">
					<div class="col-sm-12">
						<h1>Quản lý seller</h1>
					</div>
				</div>
			</div>
			<!-- /.container-fluid -->
		</section>
		<!-- /.content-wrapper -->
		<section class="content">
			<div class="container-fluid">
				<div class="row">
					<div class="col-12">
						<div class="card">
							<form action="" method="post" class="row g-3"
								enctype="multipart/form-data" style="padding: 20px;">
								<h3 class="fw-bold orangered_text col-md-12">TÀI KHOẢN</h3>
								<article class="col-md-4">
									<img id="displayedImage" alt="" src="/template/clients/asset/images/${user.avata}"
										style="width: 400px; height: 400px; background-color: black;">
									<input id="photoInput" name="photo" type="file"
										style="margin-top: 20px;">
								</article>
								<aside class="col-md-8 row" style="padding-left: 50px;">
									<div class="col-md-12">
										<label for="" class="form-label">Username</label> <input
											name="username" value="${user.userName}" type="text"
											class="form-control bg-blue-100" id="">
										<p class="errorUsername text-danger">${errorUsername}</p>
									</div>
									<div class="col-md-12">
										<label for="" class="form-label">Password</label> <input
											name="password" value="${user.password}" type="password"
											class="form-control bg-blue-100" id="">
										<p class="errorPass text-danger">${errorPass}</p>
									</div>
									<div class="col-md-12">
										<label for="inputEmail" class="form-label">Email</label> <input
											name="email" value="${user.email}" type="email"
											class="form-control bg-blue-100" id="inputEmail">
										<p class="errorEmail text-danger">${errorEmail}</p>
									</div>
									<div class="col-md-12">
										<label for="inputAdress" class="form-label">Fullname</label> <input
											name="fullname" value="${user.fullName}" type="text"
											class="form-control bg-blue-100" id="inputAdress">
										<p class="errorFullname text-danger">${errorFullname}</p>
									</div>
									<div class="col-md-12 row">
										<label class="col-md-2">Role</label>
										<div class="col-md-1">
											<input class="form-check-input" type="radio" name="role"
												value="false" <c:if test="${not user.role}">checked</c:if>
												id="flexRadioDefault3"> <label
												class="form-check-label" for="flexRadioDefault3">User</label>
										</div>
										<div class="col-md-1">
											<input class="form-check-input" type="radio" name="role"
												value="true" <c:if test="${user.role}">checked</c:if>
												id="flexRadioDefault4"> <label
												class="form-check-label" for="flexRadioDefault4">Admin</label>
										</div>
										<div class="col-md-8"></div>
									</div>
									<div class="col-md-12 ">
										<a href="/admin/user" type="submit" class="btn btn-primary"
											style="width: 100px;">Làm mới</a>
										<button type="submit" class="btn btn-success"
											style="width: 100px;">Xác nhận</button>
										<a href="/admin/user/delete?id=${user.id}" type="submit"
											class="btn btn-danger" style="width: 100px;">Xóa</a>
									</div>
								</aside>
							</form>
						</div>
						<div class="card">
							<table id="example1" class="table table-bordered table-striped">
								<thead>
									<tr>
										<th>Ảnh đại diện</th>
										<th>Tên tài khoản</th>
										<th>Họ và tên</th>
										<th>Email</th>
										<th>Vai trò</th>
										<th style="width: 150px;"></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="accounts" items="${listAccount}">
										<tr>
											<td class="align-middle"><img alt="loi"
												src="/template/clients/asset/images/${accounts.avata}" width="100" height="100"></td>
											<td class="align-middle">${accounts.userName}</td>
											<td class="align-middle">${accounts.fullName}</td>
											<td class="align-middle">${accounts.email}</td>
											<td class="align-middle">${accounts.role?'admin':'user'}</td>
											<td class="align-middle"><a
												href="/admin/user/update?id=${accounts.id}"
												class="btn btn-success"> <i class="bi bi-pencil-fill"></i>
											</a></td>
										</tr>
									</c:forEach>
								</tbody>
								<!-- <tfoot>
                      <tr>
                        <th>Rendering engine</th>
                        <th>Browser</th>
                        <th>Platform(s)</th>
                        <th>Engine version</th>
                        <th>CSS grade</th>
                      </tr>
                    </tfoot> -->
							</table>
							<!-- Card -->
						</div>
					</div>
				</div>
				<!-- /.row -->
			</div>
			<!-- /.container-fluid -->
		</section>
		<script>
			document
					.getElementById("photoInput")
					.addEventListener(
							"change",
							function(event) {
								var input = event.target;
								if (input.files && input.files[0]) {
									var reader = new FileReader();
									reader.onload = function(e) {
										document
												.getElementById("displayedImage").src = e.target.result;
									}
									reader.readAsDataURL(input.files[0]);
								}
							});
		</script>
</body>
</html>
