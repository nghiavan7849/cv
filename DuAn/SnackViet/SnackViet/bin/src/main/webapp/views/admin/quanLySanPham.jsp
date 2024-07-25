<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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

					<h1 class="h3 mb-3">Danh sách sản phẩm</h1>

					<div class="row">
						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col mt-0">
											<h5 class="card-title">Tổng số sản phẩm</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-arrow-up-right-dots"></i>
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
											<h5 class="card-title">Số sản phẩm ngưng bán</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-arrow-up-right-dots"></i>
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
					
					<div class="userDetail" style="margin-top:20px;">
						<h4> Chi tiết sản phẩm </h4>
						<form:form modelAttribute="sanPham" method="post" enctype="multipart/form-data">
							  <input type="text" hidden readonly name="idForUpdate" value="${idForUpdate }">
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Tên sản phẩm:</label>
							  			<form:input type="text" path="tenSanPham" class="form-control" placeholder="Nhập vào tên sản phẩm"></form:input>
							  			<form:errors path="tenSanPham" cssClass="text-danger"/>
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Giá:</label>
							  			<form:input type="number" path="gia" class="form-control"  placeholder="Nhập vào giá sản phẩm"></form:input>
							  			<form:errors path="gia" cssClass="text-danger"/>
							  		</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Số lượng:</label>
							  			<form:input type="number" path="soLuong" class="form-control" placeholder="Nhập vào số lượng"></form:input>
							  			<form:errors path="soLuong" cssClass="text-danger"/>
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Trọng lượng (gam):</label>
							  			<form:input type="number" path="trongLuong" class="form-control" placeholder="Nhập vào trọng lượng"></form:input>
							  			<form:errors path="trongLuong" cssClass="text-danger"/>
							  		</div>
							  	</div>
							  </div>
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Loại:</label>
<!-- 							  			<select class="form-select" aria-label="Default select example"> -->
<!-- 							  			  <option selected hidden disabled>Chọn loại</option> -->
<!-- 										  <option value="Bánh ngọt">Bánh ngọt</option> -->
<!-- 										  <option value="Bánh quy">Bánh quy</option> -->
<!-- 										  <option value="Bánh tráng">Bánh tráng</option> -->
<!-- 										  <option value="Trái cây sấy">Trái cây sấy</option> -->
<!-- 										  <option value="Kẹo">Kẹo</option> -->
<!-- 									    </select> -->
									    <form:select class="form-select" path="loaiSP.maLoai">
										    <form:option value="" selected="selected" hidden="hidden" disabled="disabled">${not empty displayP.loaiSP ? displayP.loaiSP.tenLoai : 'Chọn loại'}</form:option>
										    <form:options items="${listLoaiSP}" itemValue="maLoai" itemLabel="tenLoai"/>
										</form:select>
										<p class="text-danger">${messageSelectBox }</p>
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Trạng thái:</label>
							  			<div class="form-check">
										    <input ${editting == true ? (displayP.trangThai == true ? 'checked' : '') : 'checked'} class="form-check-input" value="true" type="radio" name="trangThai" id="flexRadioDefault1">
										    <label class="form-check-label" for="flexRadioDefault1">
										        Còn bán
										    </label>
										</div>
										<div class="form-check">
										    <input ${editting == true ? (displayP.trangThai == false ? 'checked' : '') : ''} class="form-check-input" value="false" type="radio" name="trangThai" id="flexRadioDefault2">
										    <label class="form-check-label" for="flexRadioDefault2">
										        Ngưng bán
										    </label>
										</div>
							  		</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-12">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Chọn ảnh:</label>
							  			<input name="hinhAnhData" value="${displayP.hinhAnh }" type="file" class="form-control">
							  		</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-12">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Mô tả:</label>
							  			<textarea name="moTa" class="form-control" rows="4" cols="5"><c:out value="${displayP.moTa }"></c:out></textarea> 
							  		</div>
							  	</div>
							  </div>
							   
							  <button ${editting == true ? 'disabled' : '' } formaction="/adminIndex/quan-ly-san-pham/insert" class="btn btn-info">Thêm</button>
							  <button ${editting == false ? 'disabled' : '' } formaction="/adminIndex/quan-ly-san-pham/update/${not empty displayP.maSanPham ? displayP.maSanPham : idForUpdate }" class="btn btn-info">Cập nhật</button>
							  <button ${editting == false ? 'disabled' : '' } formaction="/adminIndex/quan-ly-san-pham/cancel" class="btn btn-info">Làm mới</button>
						</form:form>
					</div>
					
					<div class="userList" style="margin-top:20px;">
						<h4 class="text-center"> Danh sách sản phẩm </h4>
						<form method="get" action="/adminIndex/quan-ly-san-pham">
							<div class="row">
								<div class="col-md-4">
									<div class="input-group mb-3">
									  <select name="sort" class="form-select" aria-label="Default select example">
									  	  <option ${not empty displaySelected ? '' : 'disabled'} selected hidden >${not empty displaySelected ? displaySelected : 'Chọn bộ lọc' }</option>
										  <option value="Loại">Loại</option>
										  <option value="Tên sản phẩm">Tên sản phẩm</option>
										  <option value="Trọng lượng">Trọng lượng</option>
										  <option value="Giá thấp tới cao">Giá thấp tới cao</option>
										  <option value="Giá cao tới thấp">Giá cao tới thấp</option>
										  <option value="Trạng thái hoạt động">Trạng thái hoạt động</option>
										  <option value="Trạng thái ngưng bán">Trạng thái ngưng bán</option>
									  </select>
									  <input value="${param.search }" name="search" type="text" class="form-control" placeholder="Tìm kiếm">
									  <button class="btn btn-success">Tìm kiếm</button>
									</div>
								</div>
							</div>
						</form>
						
						<table class="table table-striped table-hover table-light">
							<thead class="table-warning text-center">
								<th>Mã sản phẩm</th>
								<th>Tên sản phẩm</th>
								<th>Giá</th>
								<th>Số lượng</th>
								<th>Trọng lượng</th>
								<th>Loại</th>
								<th>Trạng thái</th>
								<th>Ảnh</th>
								<th>Mô tả</th>
								<th>Hành động</th>
							</thead>
							
							<tbody class="text-center">
								<c:forEach items="${products }" var="p">
									<tr>
										<td>${p.maSanPham }</td>
										<td>${p.tenSanPham }</td>
										<td>${p.gia }</td>
										<td>${p.soLuong }</td>
										<td>${p.trongLuong }</td>
										<td>${p.loaiSP.tenLoai }</td>
										<td><span class="badge ${p.trangThai == false ? 'bg-danger' : 'bg-success' }">${p.trangThai == true ? 'Còn bán' : 'Ngưng bán' }</span></td>
										<td> <img alt="" src="/image/AnhSanPham/${p.hinhAnh }" style="width: 10%"> </td>
										<td>${p.moTa }</td>
										<td> <a href="/adminIndex/quan-ly-san-pham/${p.maSanPham }"><i class="fa-solid fa-pen-to-square"></i></a> </td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						
						<div class="d-flex justify-content-between" style="margin-top:25px;">
							<!-- Vị trí hiển thị số trang -->
							<p class="fw-bold">Đang xem trang ${currentPage} / ${totalPages}</p>
		
							<!-- Cập nhật nút phân trang -->
							<nav class="pagination-outer" aria-label="Page navigation">
							    <ul class="pagination">
							        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
							            <a href="/adminIndex/quan-ly-san-pham?pageNo=${currentPage - 1}&pageSize=${pageSize}&search=${search}&sort=${sort}" class="page-link" aria-label="Previous">
							                <span aria-hidden="true">«</span>
							            </a>
							        </li>
							        <c:forEach var="page" begin="1" end="${totalPages}">
							            <li class="page-item ${currentPage == page ? 'active' : ''}">
							                <a class="page-link" href="/adminIndex/quan-ly-san-pham?pageNo=${page}&pageSize=${pageSize}&search=${search}&sort=${sort}">${page}</a>
							            </li>
							        </c:forEach>
							        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''} ">
							            <a href="/adminIndex/quan-ly-san-pham?pageNo=${currentPage + 1}&pageSize=${pageSize}&search=${search}&sort=${sort}" class="page-link" aria-label="Next">
							                <span aria-hidden="true">»</span>
							            </a>
							        </li>
							    </ul>
							</nav>
							<p class="fw-bold">5 sản phẩm / 1 trang</p>
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

