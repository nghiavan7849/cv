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
					<img id="largeImage"
						src="${request.ContextPath}/image/AnhSanPham/${sanpham.hinhAnh}"
						alt="Hình ảnh lớn" class="large-image">


					<div id="thumbnailContainer">
						<c:forEach var="h" items="${sanpham.listHinhAnhSP}">
							<img
								src="${request.ContextPath}/image/AnhSanPham/${h.tenHinhAnh}"
								alt="Thumbnail" class="thumbnail"
								onclick="changeImage('${request.ContextPath}/image/AnhSanPham/${h.tenHinhAnh}')">
						</c:forEach>
					</div>


					<!-- Slide show -->

					<!-- Slide show -->

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
						<b class="number-star" style="font-size: 25px;">${averageRating}</b>
						<c:set var="rating" value="${Double.parseDouble(averageRating)}" />

						<span style="font-size: 20px;"><c:forEach begin="1" end="5"
								var="i">
								<c:choose>
									<c:when test="${rating >= i}">
										<i class="fa fa-star text-warning"></i>
									</c:when>
									<c:when test="${rating >= (i - 0.5)}">
										<i class="fa fa-star-half-alt text-warning"></i>
									</c:when>
									<c:otherwise>
										<i class="fa fa-star text-secondary"></i>
									</c:otherwise>
								</c:choose>
							</c:forEach> </span> <span style="margin: 0 10px 0 10px; color: #979797;"> |</span>
						<span class="number-evaluate">${totalAllStars}</span> <span
							class="text-number-evaluate">Đánh giá</span> <span
							style="margin: 0 10px 0 10px; color: #979797;"> |</span> <span
							class="number-buy">${tongSoLuong}</span> <span
							class="text-number-buy">Đã bán</span>
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
									<button type="button" class=" less-salary" id="truSL">-</button>
									<input type="number" id="soLuong" min="1" class="qty"
									name="soLuong" value="1">
									<button type="button" class=" add-salary" id="congSL">+</button>
								</span> <span>${sanpham.soLuong} sản phẩm có sẵn</span>
							</div>
							<div class="button">

								<button class="btn btn-add-cart btn-outline-success"
									formaction="${request.contextPath}/chi-tiet-san-pham/them-gio-hang/${sanpham.maSanPham}"
									formmethod="post">
									<i class="bi bi-cart-plus-fill"></i> Thêm Vào giỏ hàng
								</button>
								<a href="${request.contextPath}/gio-hang/mua-hang/dat-hang?idSP=${sanpham.maSanPham}" class="btn btn-buy btn-success">Mua ngay</a>
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
					<span class="number-star">${averageRating}</span> <span
						style="color: #FAA41A; font-size: 24px; margin: 0 200px 0 0">trên
						5</span>
					<form method="get"
						action="${request.contextPath}/chi-tiet-san-pham/danhgia-more">
						<input type="hidden" name="pageNo" value="0"> <input
							type="hidden" name="pageSize" value="8"> <input
							type="hidden" name="sanPhamId" value="${sanpham.maSanPham}">
						<button type="submit" name="soSao" value=""
							class="btn btn-outline-success">Tất Cả</button>
						<button type="submit" name="soSao" value="5"
							class="btn btn-outline-success">5 sao (${count5Stars})</button>
						<button type="submit" name="soSao" value="4"
							class="btn btn-outline-success">4 sao (${count4Stars})</button>
						<button type="submit" name="soSao" value="3"
							class="btn btn-outline-success">3 sao (${count3Stars})</button>
						<button type="submit" name="soSao" value="2"
							class="btn btn-outline-success">2 sao (${count2Stars})</button>
						<button type="submit" name="soSao" value="1"
							class="btn btn-outline-success">1 sao (${count1Star})</button>
					</form>
					<div style="font-size: 45px; margin: -20px 0 0 40px;">
						<c:set var="rating" value="${Double.parseDouble(averageRating)}" />
						<c:forEach begin="1" end="5" var="i">
							<c:choose>
								<c:when test="${rating >= i}">
									<i class="fa fa-star text-warning"></i>
								</c:when>
								<c:when test="${rating >= (i - 0.5)}">
									<i class="fa fa-star-half-alt text-warning"></i>
								</c:when>
								<c:otherwise>
									<i class="fa fa-star text-secondary"></i>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</div>
				</div>
				<div class="detail-evaluate">
					<c:forEach var="dg" items="${danhGias}">
						<div class="card">
							<div class="card-body row">
								<div class="col-md-1 ms-3">
									<img alt=""
										src="${request.ContextPath}/image/avatars/${dg.taiKhoanDG.hinhAnh}"
										width="50px" height="50px" style="border-radius: 50%">
								</div>
								<div class="col-md-9">
									<div class="name">${dg.taiKhoanDG.hoVaTen}</div>

									<c:if test="${dg.soSao == 1}">
										<i class="bi bi-star-fill"></i>
									</c:if>
									<c:if test="${dg.soSao == 2}">
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
									</c:if>
									<c:if test="${dg.soSao == 3}">
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
									</c:if>
									<c:if test="${dg.soSao == 4}">
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
									</c:if>
									<c:if test="${dg.soSao == 5}">
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
										<i class="bi bi-star-fill"></i>
									</c:if>
									<div class="time-date">${dg.ngayDanhGia}</div>
								</div>
								<div class="content" style="font-size: 20px;">
									<div class="mb-3">${dg.binhLuan}</div>
									<c:forEach var="anhDG" items="${dg.listHinhAnhDG}">
										<img alt=""
											src="${request.ContextPath}/image/AnhDanhGia/${anhDG.tenHinhAnh}"
											width="72px" height="72px">
									</c:forEach>
									<!-- Kiểm tra nếu đăng nhập và đúng tài khoản đã đánh giá -->
									<c:if
										test="${sessionScope.sessionTaiKhoan != null && sessionScope.sessionTaiKhoan.maTaiKhoan == dg.taiKhoanDG.maTaiKhoan}">
										<form action="">
											<a
												href="${request.ContextPath}/danh-gia/edit/${dg.maDanhGia}">Chỉnh
												sửa</a>
										</form>
									</c:if>
								</div>
							</div>
							<hr>
						</div>
					</c:forEach>
				</div>
				<div class="see-more">
					<form action="" method="post">
						<input type="hidden" name="sanPhamId" value="${sanpham.maSanPham}">
						<button class="btn xem-them"
							formaction="${request.ContextPath}/chi-tiet-san-pham/danhgia-more?pageSizeDG=${pageSizeDG+5}">Xem
							Thêm</button>
					</form>
				</div>
			</div>

			<div class="similar-product row">
				<div style="font-size: 30px">SẢN PHẨM TƯỞNG TỰ</div>
				<%@ include file="mucsanpham.jsp"%>
			</div>
		</main>
	</div>
	<!-- Footer -->
	<%@ include file="footer.jsp"%>
	<!-- End Footer -->

	<script type="text/javascript">
	 function changeImage(imageSrc) {
         document.getElementById('largeImage').src = imageSrc;
     }
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