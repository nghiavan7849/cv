<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
	
	<!-- Lightbox CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.3/css/lightbox.min.css" rel="stylesheet" />

    <!-- Lightbox JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.3/js/lightbox-plus-jquery.min.js"></script>
	
	<style>

		.input-group {
		    display: flex;
		    align-items: center;
		}

	</style>
	
<body>
	<div class="wrapper">

		<%@ include file="templates/sideNav.jsp"%>

		<div class="main">

			<%@ include file="templates/horizontalNav.jsp"%>

			<main class="content">
				<div class="container-fluid p-0">

					<h1 class="h3 mb-3">Danh sách đánh giá sản phẩm</h1>

					<div class="row">
						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col mt-0">
											<h5 class="card-title">Tổng số bình luận</h5>
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
											<h5 class="card-title">Số sản phẩm đạt 5 sao</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-file-invoice"></i>
											</div>
										</div>
									</div>
									<h1 class="mt-1 mb-3">1893</h1>
									<div class="mb-0">
										<span class="text-success fw-bold"> <i class="mdi mdi-arrow-bottom-right"></i> 16%
										</span> <span class="text-muted">Tăng trong tháng</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					
					<div class="commentDetail d-flex justify-content-center">
						<div class="card" style="width: 1000px;height: 400px;"> <!-- Đặt chiều rộng cố định -->
							 <div class="card-body">
<!-- 							      NẾU NGƯỜI DÙNG CLICK VÀO EDIT THÌ HIỂN THỊ -->
							      <c:if test="${not empty displayR}">
					                <div class="d-flex">
					                    <img class="rounded-circle" alt="ảnh người dùng" src="${request.contextPath}/image/avatars/${displayR.taiKhoanDG.hinhAnh}" style="width: 10%;">
					                    <div class="customerDetail" style="margin-left:15px;">
					                        <p class="fw-bold">${displayR.taiKhoanDG.hoVaTen}</p>
					                        <p style="color: rgb(163, 163, 163);"><fmt:formatDate value="${displayR.ngayDanhGia}" pattern="yyyy-MM-dd"/></p>
					                    </div>
					                </div>
					                <div class="row">
					                    <div class="col-md-4">
					                        <p class="">${displayR.binhLuan}</p>
					                        <c:forEach var="i" begin="1" end="${displayR.soSao}">
					                            ⭐
					                        </c:forEach>
					                        ${displayR.soSao}
					                    </div>
					                    <div class="col-md-8 text-center">
					                        <div class="row">
					                            <c:forEach items="${listHinhDG}" var="hinh">
					                                <div style="margin-bottom:20px;" class="col-md-4 text-center">
					                                    <a href="${request.contextPath}/image/AnhDanhGia/${hinh.tenHinhAnh}" data-lightbox="review-images">
					                                        <img alt="" src="${request.contextPath}/image/AnhDanhGia/${hinh.tenHinhAnh}" style="height:120px;">
					                                    </a>
					                                </div>
					                            </c:forEach>
					                        </div>
					                    </div>
					                </div>
					            </c:if>
							      
							      <c:if test="${empty displayR }">
								      	<h2 class="text-center" style="color: grey; position: absolute; top: 50%; left: 25%;right: 25%">
								      		Chọn dữ liệu để hiển thị
								      	</h2>
							      </c:if>
							      
							</div>
						</div>
					</div>
					
					
					<div class="commentList" style="margin-top:20px;">
						<h4 class="text-center"> Đánh giá sản phẩm </h4>
						<form method="get" action="/adminIndex/danh-gia">
						    <div class="row">
						        <div class="col-md-4">
						            <div class="input-group mb-3">
						                <select id="sortSelect" name="sort" class="form-select" aria-label="Default select example" onchange="toggleSearchAndDateFields()">
						                    <option ${not empty displaySelected ? '' : 'disabled'} selected hidden >${not empty displaySelected ? displaySelected : 'Chọn bộ lọc' }</option>
						                    <option value="Ngày đánh giá">Ngày đánh giá</option>
						                    <option value="Tên sản phẩm">Tên sản phẩm</option>
						                    <option value="Số sao">Số sao</option>
						                    <option value="Trạng thái hiển thị">Trạng thái hiển thị</option>
						                    <option value="Trạng thái ẩn">Trạng thái ẩn</option>
						                    <option value="Bình luận">Bình luận</option>
						                </select>
						                <div id="searchField">
						                    <input name="search" type="text" value="${param.search}" class="form-control" placeholder="Tìm kiếm">
						                    <button class="btn btn-success">Tìm kiếm</button>
						                </div>
						            </div>
						        </div>
						
						        <div id="dateFields" class="col-md-4" style="display: none;">
						            <div class="input-group mb-3">
						                <input value="<fmt:formatDate value='${ngayFrom}' pattern='yyyy-MM-dd'/>" name="ngayFrom" type="date" class="form-control" placeholder="Từ">
						                <input value="<fmt:formatDate value='${ngayTo}' pattern='yyyy-MM-dd'/>" name="ngayTo" type="date" class="form-control" placeholder="Đến">
						                <button class="btn btn-success">Tìm kiếm</button>
						            </div>
						        </div>
						    </div>
						</form>

						
						<table class="table table-striped table-hover table-light">
							<thead class="table-warning text-center">
								<th>Mã đánh giá</th>
								<th>Ngày đánh giá</th>
								<th>Trạng thái</th>
								<th>Tên khách hàng</th>
								<th>Tên sản phẩm</th>
								<th>Bình luận</th>
								<th>Số sao</th>
								<th>Hành động</th>
							</thead>
							
							<tbody class="text-center">
								<c:forEach items="${reviews }" var="r">
									<tr>
										<td>${r.maDanhGia }</td>
										<td>${r.ngayDanhGia }</td>
										<td><span class="badge ${r.trangThai == true ? 'bg-success' : 'bg-danger' }">${r.trangThai = true ? 'Hiển thị' : 'Ẩn' }</span></td>
										<td>${r.taiKhoanDG.hoVaTen }</td>
										<td>${r.sanPhamDG.tenSanPham }</td>
										<td>${r.binhLuan }</td>
										<td>
											<c:forEach var="i" begin="1" end="${r.soSao }">
												⭐
											</c:forEach>
										</td>
										<td> 
											<a href="/adminIndex/danh-gia/${r.maDanhGia}?pageNo=${page}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=<fmt:formatDate value='${ngayFrom}' pattern='yyyy-MM-dd'/>&ngayTo=<fmt:formatDate value='${ngayTo}' pattern='yyyy-MM-dd'/>">
							                    <i class="fa-solid fa-pen-to-square"></i>
							                </a>
										</td>
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
								    <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
								        <a href="/adminIndex/danh-gia?pageNo=${currentPage - 1}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=${ngayFrom}&ngayTo=${ngayTo}" class="page-link" aria-label="Previous">
								            <span aria-hidden="true">«</span>
								        </a>
								    </li>
								    <c:forEach var="page" begin="1" end="${totalPages}">
								        <li class="page-item ${currentPage == page ? 'active' : ''}">
								            <a class="page-link" href="/adminIndex/danh-gia?pageNo=${page}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=${ngayFrom}&ngayTo=${ngayTo}">${page}</a>
								        </li>
								    </c:forEach>
								    <li class="page-item ${(totalPages == 0 || currentPage >= totalPages) ? 'disabled' : ''}">
								        <a href="/adminIndex/danh-gia?pageNo=${currentPage + 1}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=${ngayFrom}&ngayTo=${ngayTo}" class="page-link" aria-label="Next">
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
		
	
	<script>
	    function toggleSearchAndDateFields() {
	        var sortSelect = document.getElementById("sortSelect");
	        var searchField = document.getElementById("searchField");
	        var dateFields = document.getElementById("dateFields");
	        
	        if (sortSelect.value === "Ngày đánh giá") {
	            searchField.style.display = "none";
	            dateFields.style.display = "flex"; // Sử dụng flex để giữ các phần tử trong cùng một hàng
	        } else {
	            searchField.style.display = "flex"; // Sử dụng flex để giữ các phần tử trong cùng một hàng
	            dateFields.style.display = "none";
	        }
	    }
	    
	    // Gọi hàm này khi trang được tải để đặt trạng thái ban đầu
	    window.onload = toggleSearchAndDateFields;
	</script>
	


</body>
</html>

