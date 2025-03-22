<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<link rel="preconnect" href="https://fonts.gstatic.com">
<link rel="shortcut icon" href="img/icons/icon-48x48.png" />

<link rel="canonical" href="https://demo-basic.adminkit.io/" />
<link href="/css/app.css" rel="stylesheet">
<link href="/css/pagination.css" rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<!-- FontAwesome 6.2.0 CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css"
	integrity="sha512-xh6O/CkQoPOWDdYTDqeRdPCVd1SpvCA9XXcUnZS2FmJNp1coAFzvtCN9BmamE+4aHK8yyUHUSCcJHgXloTyT2A=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />

<!-- (Optional) Use CSS or JS implementation -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/js/all.min.js"
	integrity="sha512-naukR7I+Nk6gp7p5TMA4ycgfxaZBJ7MO5iC3Fp6ySQyKFHOGfpkSZkYVWV5R7u7cfAicxanwYQ5D1e17EfJcMA=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<body>
	<div class="wrapper">

		<%@ include file="templates/sideNav.jsp"%>

		<div class="main">

			<%@ include file="templates/horizontalNav.jsp"%>

			<main class="content">
				<div class="container-fluid p-0">

					<h1 class="h3 mb-3">Danh sách hóa đơn</h1>

					<div class="row">
						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col mt-0">
											<h5 class="card-title">Tổng số hóa đơn</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-file-invoice"></i>
											</div>
										</div>
									</div>
									<h1 class="mt-1 mb-3">5423</h1>
									<div class="mb-0">
										<span class="text-success fw-bold"> <i class="mdi mdi-arrow-bottom-right"></i> 16%
										</span> <span class="text-muted">Tăng trong tháng</span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col mt-0">
											<h5 class="card-title">Số hóa đơn bị hủy</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-file-invoice"></i>
											</div>
										</div>
									</div>
									<h1 class="mt-1 mb-3">1893</h1>
									<div class="mb-0">
										<span class="text-danger fw-bold"> <i class="mdi mdi-arrow-bottom-right"></i> 16%
										</span> <span class="text-muted">Tăng trong tháng</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div class="invoiceDetail" style="margin-top:20px;">
						<h4> Hóa đơn chi tiết </h4>
						<form method="post" action="">
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Mã hóa đơn chi tiết:</label>
							  			<input type="text" class="form-control" readonly="readonly" disabled="disabled" value="59127">
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Số lượng:</label>
							  			<input type="number" class="form-control"  value="48">
							  		</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Tổng tiền sản phẩm:</label>
							  			<input type="text" class="form-control" value="150.000">
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Ghi chú:</label>
							  			<textarea class="form-control" rows="5" cols="2">Okay</textarea>
							  		</div>
							  	</div>
							  </div> 
							  
						</form>
					</div>
					
					<div class="invoiceList" style="margin-top:20px;">
						<h4 class="text-center"> Danh sách hóa đơn </h4>
						<div class="row">
							<div class="col-md-4">
								<div class="input-group mb-3">
								  <select class="form-select" aria-label="Default select example">
									  <option selected>Mã hóa đơn</option>
									  <option value="Tên khách hàng">Tên khách hàng</option>
									  <option value="Ngày thanh toán">Ngày thanh toán</option>
									  <option value="Trạng thái">Trạng thái</option>
								  </select>
								  <input type="text" class="form-control" placeholder="Tìm kiếm">
								  <button class="btn btn-success">Tìm kiếm</button>
								</div>
							</div>
						</div>
						
						<table class="table table-striped table-hover table-light">
							<thead class="table-warning text-center">
								<th>Mã hóa đơn</th>
								<th>Ngày thanh toán</th>
								<th>Trạng thái</th>
								<th>Tên khách hàng</th>
								<th>Tổng tiền</th>
								<th>Địa chỉ</th>
								<th>Hành động</th>
							</thead>
							
							<tbody class="text-center">
								<tr>
									<td>59217</td>
									<td>20/12/2024</td>
									<td><span class="badge bg-success">Đã thanh toán</span></td>
									<td>Cody Fisher</td>
									<td>150.000</td>
									<td>KFC LOTTE Mart Cần Thơ, 84 Đ. Mậu Thân, Xuân Khánh, Ninh Kiều, Cần Thơ</td>
									<td> <a href="#"><i class="fa-solid fa-pen-to-square"></i></a> </td>
								</tr>
								<tr>
									<td>59213</td>
									<td>20/12/2024</td>
									<td><span class="badge bg-danger">Đã hủy</span></td>
									<td>Kristin Watson</td>
									<td>150.000</td>
									<td>KFC LOTTE Mart Cần Thơ, 84 Đ. Mậu Thân, Xuân Khánh, Ninh Kiều, Cần Thơ</td>
									<td> <a href="#"><i class="fa-solid fa-pen-to-square"></i></a> </td>
								</tr>
								<tr>
									<td>59219</td>
									<td>20/12/2024</td>
									<td><span class="badge bg-success">Đã thanh toán</span></td>
									<td>Esther Howard</td>
									<td>150.000</td>
									<td>KFC LOTTE Mart Cần Thơ, 84 Đ. Mậu Thân, Xuân Khánh, Ninh Kiều, Cần Thơ</td>
									<td> <a href="#"><i class="fa-solid fa-pen-to-square"></i></a> </td>
								</tr>
								
							</tbody>
	
						</table>
						
						<div class="d-flex justify-content-between" style="margin-top:25px;">
							<p class="fw-bold">Đang xem trang 1 / 30</p>
							<nav class="pagination-outer" aria-label="Page navigation">
								<ul class="pagination">
									<li class="page-item">
									    <a href="#" class="page-link" aria-label="Previous">
									    	<span aria-hidden="true">«</span>
									    </a>
									</li>
									<li class="page-item"><a class="page-link" href="#">1</a></li>
									<li class="page-item"><a class="page-link" href="#">2</a></li>
									<li class="page-item active"><a class="page-link" href="#">3</a></li>
									<li class="page-item"><a class="page-link" href="#">4</a></li>
									<li class="page-item"><a class="page-link" href="#">5</a></li>
									<li class="page-item">
									    <a href="#" class="page-link" aria-label="Next">
									       <span aria-hidden="true">»</span>
									    </a>
									</li>
								</ul>
							</nav>
						</div>
					</div>
					
				</div>
			</main>

			<%@ include file="templates/footer.jsp"%>

		</div>


	</div>

	<script src="/js/app.js"></script>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>


</body>
</html>

