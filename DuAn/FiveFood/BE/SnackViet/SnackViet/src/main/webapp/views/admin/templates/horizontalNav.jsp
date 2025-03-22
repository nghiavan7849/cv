<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
			<nav class="navbar navbar-expand navbar-light navbar-bg">
				<a class="sidebar-toggle js-sidebar-toggle"> <i
					class="hamburger align-self-center"></i>
				</a>

				<div class="navbar-collapse collapse">
					<ul class="navbar-nav navbar-align">
						<li class="nav-item dropdown">
							<a class="nav-icon dropdown-toggle d-inline-block d-sm-none" href="#" data-bs-toggle="dropdown"> <i class="align-middle" data-feather="settings"></i></a> 
							<a class="nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-bs-toggle="dropdown"> 
								<img src="/image/avatars/${sessionScope.sessionTaiKhoan.hinhAnh }" class="avatar img-fluid rounded me-1" alt="avatar" /> <span class="text-dark">${sessionScope.sessionTaiKhoan.hoVaTen }</span>
							</a>
							<div class="dropdown-menu dropdown-menu-end">
								<a class="dropdown-item" href="${request.contextPath}/cap-nhat-tai-khoan"><i class="align-middle me-1" data-feather="user"></i> Thông tin cá nhân</a>
								<a class="dropdown-item" href="/home-index"><i class="align-middle me-1" data-feather="user"></i> Về trang người dùng</a>
								<div class="dropdown-divider"></div>
								<a class="dropdown-item" href="${request.contextPath}/dang-xuat">Log out</a>
							</div></li>
					</ul>
				</div>
			</nav>	
			<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
			
			
</body>
</html>