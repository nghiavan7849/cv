<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đánh Giá</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	main="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
<link rel='stylesheet prefetch'
	href='https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css'>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/danhgia.css">
    <link rel="stylesheet" href="${request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>

	<!-- Menu -->
   <%@ include file="menu.jsp" %>
   <!-- End Menu -->

	<div class="">
		<div class="main">
			<b>ĐÁNH GIÁ SẢN PHẨM</b>
			<div class="stars">
				<form action="">
					<input class="star star-5" id="star-5" type="radio" name="star" />
					<label class="star star-4" for="star-5"></label> <input
						class="star star-4" id="star-4" type="radio" name="star" /> <label
						class="star star-4" for="star-4"> </label> <input
						class="star star-3" id="star-3" type="radio" name="star" /> <label
						class="star star-3" for="star-3"></label> <input
						class="star star-2" id="star-2" type="radio" name="star" /> <label
						class="star star-2" for="star-2"></label> <input
						class="star star-1" id="star-1" type="radio" name="star" /> <label
						class="star star-1" for="star-1"></label>
				</form>
			</div>
			<div class="content"> 
				<label>Hình ảnh</label><br>
				<input class="mb-3" type="file"><br>
				<textarea rows="5" cols="80" placeholder="Nội dung"></textarea><br>
				<div class="content-btn mt-3" style="text-align: right;">
					<button class="btn btn-secondary me-3">Thoát</button>
					<button class="btn btn-warning">Đánh giá</button>
				</div>
			</div>
		</div>
	</div>
	
	    <!-- Footer -->
    <%@ include file="footer.jsp" %>
    <!-- End Footer -->
    
</body>
</html>