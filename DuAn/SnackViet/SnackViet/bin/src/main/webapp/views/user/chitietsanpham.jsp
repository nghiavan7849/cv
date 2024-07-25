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
	<!-- Menu -->
	<%@ include file="menu.jsp"%>
	<!-- End Menu -->
	<div class="bg-content pt-4 pb-5">
		<main class="container ">
			<div class="detail row">
				<div class="image-product col-md-5">
					<img class="image-big mb-3" alt=""
						src="${request.ContextPath}/image/AnhSanPham/${sanpham.hinhAnh}"
						width="400px" height="316px">
					<c:forEach var="h" items="${anhSPs}">
						<a
							href="${request.ContextPath}/chi-tiet-san-pham/anh/${h.tenHinhAnh}"
							style="text-decoration: none;"> <img class="image" alt=""
							src="${request.ContextPath}/image/AnhSanPham/${h.tenHinhAnh}"
							width="92px" height="92px">
						</a>
					</c:forEach>


					<div class="share">
						<b>Chia sẽ: </b> <a class="link-share" href="#"><i
							class="bi icon-share bi-messenger" style="width: 100px"></i></a> <a
							class="link-share" href="#"><i
							class="bi icon-share bi-facebook" style="color: blue;"></i></a> <a
							class="link-share" href="#"><i
							class="bi icon-share bi-twitter"></i></a>
					</div>
				</div>
				<div class="detail-product col-md-6">
					<div class="name-product">${sanpham.tenSanPham}</div>
					<div class="star">
						<b class="number-star">5</b> <i class="bi bi-star-fill"></i> <i
							class="bi bi-star-fill"></i> <i class="bi bi-star-fill"></i> <i
							class="bi bi-star-fill"></i> <i class="bi bi-star-fill"></i> <span
							style="margin: 0 10px 0 10px; color: #979797;"> |</span> <span
							class="number-evaluate">490</span> <span
							class="text-number-evaluate">Đánh giá</span> <span
							style="margin: 0 10px 0 10px; color: #979797;"> |</span> <span
							class="number-buy">1,1k</span> <span class="text-number-buy">Đã
							bán</span>
					</div>
					<div class="detail-product2">
						<div class="price">
							<span><fmt:formatNumber value="${sanpham.gia}"
									pattern=" #,###" /> VNĐ</span>
						</div>
						<div class="description">
							<span>${sanpham.moTa}</span>
						</div>
						<form>
							<div class="salary">
								<span>Số lượng</span> <span class="box-add-salary">
									<button type="button" class=" less-salary" id="truSL">-</button> <input type="number" id="soLuong" min="1" class="qty"
									name="soLuong" value="1">
									<button type="button" class=" add-salary" id="congSL">+</button>
								</span> <span>2029 sản phẩm có sẵn</span>
							</div>
							<div class="button">
							
								<button class="btn btn-add-cart btn-outline-success" formaction="${request.contextPath}/chi-tiet-san-pham/them-gio-hang/1" formmethod="post">
									<i class="bi bi-cart-plus-fill"></i> Thêm Vào giỏ hàng
								</button>
								<button class="btn btn-buy btn-success">Mua ngay</button>
							</div>
						</form>
						<hr>
						<div>
							<span style="margin: 0 40px 0 0;"><i
								class="bi bi-arrow-repeat" style="color: red;"></i> Hàng chất
								lượng 100%</span> <span><i class="bi bi-truck"
								style="color: red"></i> Miễn phí vận chuyển</span>
						</div>
					</div>
				</div>
			</div>
			<hr>
			<div class="evaluate">
				<div style="font-size: 30px">ĐÁNH GIÁ SẢN PHẨM</div>
				<div class="detail-evaluate-star">
					<span class="number-star">5</span> <span
						style="color: #FAA41A; font-size: 24px; margin: 0 200px 0 0">trên
						5</span>
					<button class="btn btn-outline-success">Tất Cả</button>
					<button class="btn btn-outline-success">5 sao (490)</button>
					<button class="btn btn-outline-success">4 sao (0)</button>
					<button class="btn btn-outline-success">3 sao (0)</button>
					<button class="btn btn-outline-success">2 sao (0)</button>
					<button class="btn btn-outline-success">1 sao (0)</button>
					<div>
						<i class="bi bi-star-fill" style="margin: 0 0 0 40px;"></i> <i
							class="bi bi-star-fill"></i> <i class="bi bi-star-fill"></i> <i
							class="bi bi-star-fill"></i> <i class="bi bi-star-fill"></i>
					</div>
				</div>
				<div class="detail-evaluate">

					<div class="card">
						<div class="card-body row">
							<div class="col-md-1 ms-3">
								<img alt="" src="${request.ContextPath}/image/avatar.png"
									width="50px" height="50px">
							</div>
							<div class="col-md-9">
								<div class="name">DinhVanPhat</div>
								<i class="bi bi-star-fill"></i> <i class="bi bi-star-fill"></i>
								<i class="bi bi-star-fill"></i> <i class="bi bi-star-fill"></i>
								<i class="bi bi-star-fill"></i>
								<div class="time-date">2024-05-21 20:13</div>
							</div>
							<div class="content" style="font-size: 20px;">
								<div class="mb-3">Được bạn bè giới thiệu nên mua dùng thử,
									đóng gói cẩn thận, giao hàng nhanh.</div>

								<img alt="" src="${request.ContextPath}/image/imageComment.png"
									width="72px" height="72px">
							</div>
						</div>
						<hr>
					</div>


				</div>

				<div class="similar-product row">
					<div style="font-size: 30px">SẢN PHẨM TƯỞNG TỰ</div>
					<%@ include file="mucsanpham.jsp"%>
				</div>
		</main>
	</div>
		<!-- Footer -->
    	<%@ include file="footer.jsp" %>
    <!-- End Footer -->
    
    <script type="text/javascript">
	  //3.Tăng giảm số lượng
	  	document.getElementById('congSL').addEventListener('click',()=>{
			var value = parseInt(document.getElementById('soLuong').value);
	    	value++;
	    	document.getElementById('soLuong').value = value;
	 	});
		document.getElementById('truSL').addEventListener('click',()=>{
			var value = parseInt(document.getElementById('soLuong').value);
		    value = isNaN(value) ? 1 : value;
		    if (value > 1) {
		        value--;
		        document.getElementById('soLuong').value = value;
		    }
	 	}); 

    </script>
</body>
</html>