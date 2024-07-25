<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Shopping Cart</title>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet"
	href="${request.contextPath}/css/lichsumuahang.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>

	<!-- Menu -->
	<%@ include file="menu.jsp"%>
	<!-- End Menu -->


	<h4 class="cart-header mt-4">Lịch sử mua hàng</h4>
	<div class="container cart-container mb-5">

		<ul class="nav nav-tabs" id="myTab" role="tablist">
			<li class="nav-item"><a class="nav-link active"
				id="purchased-tab" data-toggle="tab" href="#purchased" role="tab"
				aria-controls="purchased" aria-selected="true">Đã Mua</a></li>
			<li class="nav-item"><a class="nav-link" id="cancelled-tab"
				data-toggle="tab" href="#cancelled" role="tab"
				aria-controls="cancelled" aria-selected="false">Đã Hủy</a></li>
			<li class="nav-item"><a class="nav-link" id="confirmed-tab"
				data-toggle="tab" href="#confirmed" role="tab"
				aria-controls="confirmed" aria-selected="false">Đã Xác Nhận</a></li>
			<li class="nav-item"><a class="nav-link" id="processed-tab"
				data-toggle="tab" href="#processed" role="tab"
				aria-controls="processed" aria-selected="false">Đang Xử Lý</a></li>
			<li class="nav-item"><a class="nav-link" id="shipped-tab"
				data-toggle="tab" href="#shipped" role="tab" aria-controls="shipped"
				aria-selected="false">Đã Vận Chuyển</a></li>
			<li class="nav-item"><a class="nav-link" id="delivered-tab"
				data-toggle="tab" href="#delivered" role="tab"
				aria-controls="delivered" aria-selected="false">Giao Thành Công</a>
			</li>
			<li class="nav-item"><a class="nav-link" id="failed-tab"
				data-toggle="tab" href="#failed" role="tab" aria-controls="failed"
				aria-selected="false">Giao Thất Bại</a></li>
		</ul>
		<!-- Đã Mua -->
		<div class="tab-content" id="myTabContent">
			<div class="tab-pane fade show active" id="purchased" role="tabpanel"
				aria-labelledby="purchased-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Hoàn Thành</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			<!-- Đã Hủy -->
			<div class="tab-pane fade" id="cancelled" role="tabpanel"
				aria-labelledby="cancelled-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Đã hủy</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			<!-- Đã Xác Nhận -->
			<div class="tab-pane fade" id="confirmed" role="tabpanel"
				aria-labelledby="confirmed-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Đã Xác Nhận</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			
			<!-- Đang Xử lý -->
			<div class="tab-pane fade" id="processed" role="tabpanel"
				aria-labelledby="processed-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Đang Xử lý</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			<!-- Đang Vận Chuyển  -->
			<div class="tab-pane fade" id="shipped" role="tabpanel"
				aria-labelledby="shipped-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Đang vận chuyển</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			<!-- Giao thành công -->
			<div class="tab-pane fade" id="delivered" role="tabpanel"
				aria-labelledby="delivered-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Giao thành công</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			<!-- Giao thất bại -->
			<div class="tab-pane fade" id="failed" role="tabpanel"
				aria-labelledby="failed-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Giao thất bại</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
			<div class="tab-pane fade" id="cancelled" role="tabpanel"
				aria-labelledby="cancelled-tab">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>Sản phẩm</th>
								<th>Trạng Thái</th>
								<th>Số Lượng</th>
								<th>Số Tiền</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<tr class="cart-item">
								<td class="align-content-center">
									<div class="product-info">
										<img src="image/banh-snack.jpg" alt="Product Image">
										Bánh tráng
									</div>
								</td>
								<td class="completed align-content-center">Giao thất bại</td>
								<td class="align-content-center">x10</td>
								<td class="align-content-center">100.000đ</td>
								<td class="align-content-center"><a
									class="btn btn-sm btn-buy-again">Mua Lại</a> <a
									href="${request.contextPath}/danh-gia"
									class="btn btn-sm btn-review">Đánh giá</a></td>
							</tr>
							<!-- Repeat for other items -->
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="text-center">
			<button class="btn btn-secondary">Xem thêm</button>
		</div>
	</div>

	<!-- Footer -->
	<%@ include file="footer.jsp"%>
	<!-- End Footer -->

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>