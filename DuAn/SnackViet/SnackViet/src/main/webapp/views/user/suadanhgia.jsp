<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa Đánh Giá</title>
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
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/danhgia.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>
	<!-- Menu -->
	<%@ include file="menu.jsp"%>
	<!-- End Menu -->

	<div class="main">
		<b>CHỈNH SỬA ĐÁNH GIÁ</b>

		<form>

			<table class="table">
				<thead>
					<tr>
						<th>Ảnh</th>
						<th>Tên Sản Phẩm</th>
						<th>Số Lượng</th>
						<th>Số Tiền</th>
					</tr>
				</thead>
				<tbody>
					<tr class="cart-item">
						<td class="align-content-center">
							<div class="product-info">
								<!-- ảnh trong bản lịch sử mua -->
								<span><img alt=""
									src="${request.contextPath}/image/AnhSanPham/${sanpham.hinhAnh}"
									width="150px"></span>
							</div>
						</td>
						<td class="align-content-center">${sanpham.tenSanPham}<!-- tên trong bản lịch sử mua --></td>
						<td class="align-content-center">${soLuong}</td>
						<!-- số lượng trong bản lịch sử mua -->
						<td class="align-content-center" style="color: red;"><fmt:formatNumber
								value="${tongTien}" pattern=" #,###" /> đ</td>
						<!-- giá trong bản lịch sử mua -->
					</tr>
				</tbody>
			</table>
		</form>

		<form action="${request.contextPath}/danh-gia/update" method="post"
			enctype="multipart/form-data">
			<div class="stars">
				<input type="hidden" name="idDanhGia" value="${danhGia.maDanhGia}">
				<input type="hidden" name="masanpham"
					value="${danhGia.sanPhamDG.maSanPham}"> <input
					class="star star-5" value="5" id="star-5" type="radio" name="star"
					${danhGia.soSao == 5 ? 'checked' : ''} /> <label
					class="star star-5" for="star-5"></label> <input
					class="star star-4" value="4" id="star-4" type="radio" name="star"
					${danhGia.soSao == 4 ? 'checked' : ''} /> <label
					class="star star-4" for="star-4"></label> <input
					class="star star-3" value="3" id="star-3" type="radio" name="star"
					${danhGia.soSao == 3 ? 'checked' : ''} /> <label
					class="star star-3" for="star-3"></label> <input
					class="star star-2" value="2" id="star-2" type="radio" name="star"
					${danhGia.soSao == 2 ? 'checked' : ''} /> <label
					class="star star-2" for="star-2"></label> <input
					class="star star-1" value="1" id="star-1" type="radio" name="star"
					${danhGia.soSao == 1 ? 'checked' : ''} /> <label
					class="star star-1" for="star-1"></label>
			</div>
			<div class="content">
				<input name="masanpham" class="mb-3" type="number" hidden=""
					value="${sanpham.maSanPham}"><label
					style="margin-top: 100px">Hình ảnh (tối đa 4)</label>
				<div class="image-input">

					<c:forEach items="${anhDG}" var="hinhAnh" varStatus="status">
						<div class="image-container">
							<img id="previewImage${status.index + 1}"
								src="${request.contextPath}/image/AnhDanhGia/${hinhAnh.tenHinhAnh}"
								alt="Chọn ảnh" /> <input name="image${status.index + 1}"
								id="image${status.index + 1}" class="mb-3" type="file"
								onchange="previewImage(event, 'previewImage${status.index + 1}')"
								style="width: 150px">
						</div>
					</c:forEach>
				</div>
				<textarea name="content" rows="5" cols="80" placeholder="Nội dung">${danhGia.binhLuan}</textarea>
				<br>
				<div class="content-btn mt-3" style="text-align: right;">
					<button class="btn btn-out btn-secondary me-3"
						formaction="${request.contextPath}/danh-gia">Hủy</button>
					<button type="submit">Lưu chỉnh sửa</button>
				</div>

			</div>
		</form>
	</div>

	<!-- Footer -->
	<%@ include file="footer.jsp"%>
	<!-- End Footer -->
	<script type="text/javascript">
		function previewImage(event, previewId) {
			var reader = new FileReader();
			var image = document.getElementById(previewId);

			reader.onload = function() {
				if (reader.readyState == 2) {
					image.src = reader.result;
				}
			}

			reader.readAsDataURL(event.target.files[0]);
		}
	</script>
</body>
</html>
</body>
</html>