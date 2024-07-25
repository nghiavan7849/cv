<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detail Product</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link rel="stylesheet"
	href="${request.ContextPath}/css/chitietsanpham.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>
	<c:forEach var="sp" items="${sanPhams}">
		<div class="col-md-3">
			<div class="card">
				<a href="${request.ContextPath}/chi-tiet-san-pham/${sp.maSanPham}">
					<img src="${request.ContextPath}/image/AnhSanPham/${sp.hinhAnh}"
					class="card-img-top" height="280px">
				</a>
				<div class="card-body">
					<h3 class="product-name">${sp.tenSanPham}</h3>
					<span class="product-price"><fmt:formatNumber
							value="${sp.gia}" pattern=" #,###" /> VNĐ</span>
					<button class="btn btn-success" style="margin: 0 0 0 10px">Mua
						ngay</button>
				</div>
			</div>
			<br>
		</div>
	</c:forEach>
	<div class="see-more">
		<form action="" method="post">
			<button class="btn xem-them"
				formaction="${request.ContextPath}/chi-tiet-san-pham?pageSize=${pageSize+4}">Xem
				Thêm</button>
		</form>
	</div>
</body>
</html>