<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://fonts.googleapis.com/css2?family=Rancho&display=swap" rel="stylesheet">
</head>
<body>
	<nav id="sidebar" class="sidebar js-sidebar">
			<div class="sidebar-content js-simplebar">
				<a class="sidebar-brand" href="${request.contextPath}/adminIndex"> <span
					class="align-middle" style="font-family: Rancho;">Snacks Việt</span>
				</a>

				<ul class="sidebar-nav">
<!-- 					<li class="sidebar-header">Pages</li> -->

					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex"> <i class="fa-solid fa-house"></i> <span class="align-middle">Dashboard</span>
						</a>
					</li>

					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex/quan-ly-hoa-don"> <i class="fa-solid fa-receipt"></i> <span class="align-middle">Quản lý Hóa đơn</span> </a>
					</li>

					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex/quan-ly-nguoi-dung"> <i class="fa-solid fa-users"></i> <span class="align-middle">Quản lý Người dùng</span></a>
					</li>
					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex/quan-ly-san-pham"> <i class="fa-solid fa-cookie-bite"></i> <span class="align-middle">Quản lý Sản phẩm</span></a>
					</li>
					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex/quan-ly-loai-san-pham"> <i class="fa-solid fa-cookie-bite"></i> <span class="align-middle">Quản lý Loại sản phẩm</span></a>
					</li>
					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex/thong-ke"> <i class="fa-solid fa-chart-simple"></i> <span class="align-middle">Thống kê</span></a>
					</li>

					<li class="sidebar-item">
						<a class="sidebar-link" href="${request.contextPath}/adminIndex/danh-gia"> <i class="fa-solid fa-star"></i> <span class="align-middle">Đánh giá</span></a>
					</li>

				

					

					<li class="sidebar-header"></li>

					
				</ul>


			</div>
		</nav>
		
    	
</body>
</html>