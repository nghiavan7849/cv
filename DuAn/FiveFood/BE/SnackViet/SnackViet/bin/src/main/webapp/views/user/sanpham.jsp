<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sản phẩm</title>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="${request.contextPath}/css/sanpham.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>

	<!-- Menu -->
	<%@ include file="menu.jsp"%>
	<!-- End Menu -->

	<div class="container product-container mt-4 mb-4">
    <div class="row">
        <div class="col-md-3">
            <div class="filters">
                <div class="search-container">
                    <input type="text" class="search-input" placeholder="Tìm kiếm...">
                    <button type="submit" class="search-button"><i class="fa fa-search"></i></button>
                </div>
                <h3>Danh mục</h3>
                <ul class="categories">
                    <li><a href="#"><i class="fas fa-cookie-bite"></i> Snack <span>(12)</span></a></li>
                    <li><a href="#"><i class="fas fa-bread-slice"></i> Bánh tráng <span>(8)</span></a></li>
                    <li><a href="#"><i class="fas fa-candy-cane"></i> Kẹo <span>(15)</span></a></li>
                    <li><a href="#"><i class="fas fa-ice-cream"></i> Kem <span>(7)</span></a></li>
                    <li><a href="#"><i class="fas fa-coffee"></i> Đồ uống <span>(20)</span></a></li>
                </ul>

					<h3>Giá</h3>
					<div class="form-group">
						<input type="range" class="form-control-range" min="0" max="100"
							id="priceRange"> <span id="priceValue" >0 VND</span>
					</div>

					<h3>Bổ sung</h3>
					<div class="form-group">
						<div>
							<label><input type="radio" name="additional"
								value="organic"> Hữu cơ</label>
						</div>
						<div>
							<label><input type="radio" name="additional"
								value="fresh"> Tươi mới</label>
						</div>
						<div>
							<label><input type="radio" name="additional"
								value="sales"> Bán chạy</label>
						</div>
						<div>
							<label><input type="radio" name="additional"
								value="discount"> Giảm giá</label>
						</div>
						<div>
							<label><input type="radio" name="additional"
								value="expired"> Hết hạn</label>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-9">
				<div class="sort-options">
					<label for="sort">Sắp xếp theo: </label> <select
						class="form-control d-inline w-auto" id="sort">
						<option>Bán chạy nhất</option>
						<option>Giá tăng dần</option>
						<option>Giá giảm dần</option>
					</select>
				</div>
				<div class="similar-product row container-center">
					<div class="col-md-4">
						<div class="card">
							<a href="${request.contextPath}/chi-tiet-san-pham"> <img
								src="/image/img.png" class="card-img-top" alt="...">
							</a>
							<div class="card-body">
								<h3 class="product-name">Snack Mực cay</h3>
								<div class="action">
									<span class="product-price">10.000 VND</span>
									<button class="btn btn-success">Mua ngay</button>
								</div>
							</div>
						</div>
					</div>
					<!-- Repeat the above card structure for the remaining images -->
				</div>
			</div>
		</div>
	</div>

	<!-- Footer -->
	<%@ include file="footer.jsp"%>
	<!-- End Footer -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script>
		// JavaScript to display price range value
		const priceRange = document.getElementById('priceRange');
		const priceValue = document.getElementById('priceValue');

		priceRange.addEventListener('input', function() {
			priceValue.textContent = priceRange.value + ' VND';
		});
	</script>
</body>
</html>
