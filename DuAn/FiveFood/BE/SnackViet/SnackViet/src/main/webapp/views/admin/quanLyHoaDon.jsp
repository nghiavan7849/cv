<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
	<!-- SweetAlert2 CSS -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@10"></link>
	
	<style>

		.input-group {
		    display: flex;
		    align-items: center;
		}

	</style>

</head>
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
									<h1 class="mt-1 mb-3">
										${fn:length(total) }
									</h1>
<!-- 									<div class="mb-0"> -->
<!-- 										<span class="text-success fw-bold"> <i class="mdi mdi-arrow-bottom-right"></i> 16% -->
<!-- 										</span> <span class="text-muted">Tăng trong tháng</span> -->
<!-- 									</div> -->
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
									<h1 class="mt-1 mb-3">
										${fn:length(canceled) }
									</h1>
<!-- 									<div class="mb-0"> -->
<!-- 										<span class="text-danger fw-bold"> <i class="mdi mdi-arrow-bottom-right"></i> 16% -->
<!-- 										</span> <span class="text-muted">Tăng trong tháng</span> -->
<!-- 									</div> -->
								</div>
							</div>
						</div>
					</div>
					
					<div class="invoiceDetail" style="margin-top:20px;">
						<h4> Hóa đơn chi tiết </h4>
						<form method="post" action="">
							 <div class="row">
							 	<div class="col-md-8">
							 		<table class="table table-striped table-hover table-light">
										<thead class="table-warning text-center">
											<th>Tên sản phẩm</th>
											<th>Số lượng</th>
										</thead>
										
										<tbody class="text-center">
											<c:forEach items="${displayI }" var="detail">
												<tr>
													<td>${detail.sanPhamCT.tenSanPham }</td>
													<td>${detail.soLuong }</td>
												</tr>
											</c:forEach>								
										</tbody>
									</table>	
							 	</div>
							 	
							 	<div class="col-md-4">
									<div class="mb-3">
								  		<label class="form-label fw-bold">Trạng thái:</label>
								  		<select name="trangThaiHoaDon.tenTrangThai" class="form-select" aria-label="Default select example">
								  			<option value="${not empty displayHoaDon.trangThaiHoaDon ? displayHoaDon.trangThaiHoaDon.tenTrangThai : 'Chọn trang thái'}" selected hidden>${not empty displayHoaDon.trangThaiHoaDon ? displayHoaDon.trangThaiHoaDon.tenTrangThai : 'Chọn trang thái'}</option>
											<c:forEach items="${listTrangThai }" var="trangThaiHoaDon">
											  	<option value="${trangThaiHoaDon.tenTrangThai }">${trangThaiHoaDon.tenTrangThai }</option>
											</c:forEach>
										</select>
										<em class="text-danger">${errType }</em>
								  	</div>
								  	<button ${editting == true ? '' : 'disabled' } formaction="/adminIndex/quan-ly-hoa-don/update/${not empty displayHoaDon.maHoaDon ? displayHoaDon.maHoaDon : idForUpdate }" class="btn btn-info">Cập nhật trạng thái đơn hàng</button>
								</div>
							 	
							 </div>   
						</form>
					</div>
					
					<div class="invoiceList" style="margin-top:20px;">
						<h4 class="text-center"> Danh sách hóa đơn </h4>
						<form action="/adminIndex/quan-ly-hoa-don" method="get">
							<div class="row">
								<div class="col-md-4">
									<div class="input-group mb-3">
									  <select id="sortSelect" name="sort" onchange="toggleSearchAndDateFields()" class="form-select" aria-label="Default select example">
									  	  <option ${not empty displaySelected ? '' : 'disabled'} selected hidden >${not empty displaySelected ? displaySelected : 'Chọn bộ lọc' }</option>
										  <option value="Mã hóa đơn">Mã hóa đơn</option>
										  <option value="Tên khách hàng">Tên khách hàng</option>
										  <option value="Ngày thanh toán">Ngày thanh toán</option>
										  <option value="Trạng thái">Trạng thái</option>
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
								<th>Mã hóa đơn</th>
								<th>Ngày thanh toán</th>
								<th>Trạng thái</th>
								<th>Tên khách hàng</th>
								<th>Tổng tiền sản phẩm</th>
								<th>Địa chỉ</th>
								<th>Ghi chú</th>
								<th>Phí vận chuyển</th>
								<th>Phương thức thanh toán</th>
								<th>Tổng cộng</th>
								<th>Hành động</th>
							</thead>
							
							<tbody class="text-center">
								<c:forEach items="${invoices }" var="invoice">
									<tr>
										<td>${invoice.maHoaDon }</td>
										<td>${invoice.ngayThanhToan }</td>
										<td>
											<span class="badge 
										        <c:choose>
										            <c:when test="${invoice.trangThaiHoaDon.tenTrangThai == 'Đã Hủy'}">
										                bg-danger
										            </c:when>
										            <c:when test="${invoice.trangThaiHoaDon.tenTrangThai == 'Đang xử lý '}">
										                bg-warning
										            </c:when>
										            <c:when test="${invoice.trangThaiHoaDon.tenTrangThai == 'Đang vận chuyển'}">
										                bg-info
										            </c:when>
										            <c:otherwise>
 										                bg-success 
										            </c:otherwise>
										        </c:choose>">
										        ${invoice.trangThaiHoaDon.tenTrangThai}
										    </span>
										</td>
										<td>${invoice.taiKhoanHD.hoVaTen }</td>
										<td><fmt:formatNumber pattern="#,##0 'VNĐ'" value="${invoice.tongTien }" /></td>
										<td>${invoice.diaChi }</td>
										<td>${invoice.ghiChu }</td>
										<td><fmt:formatNumber pattern="#,##0 'VNĐ'" value="${invoice.phiVanChuyen }" /></td>
										<td>${invoice.phuongThucThanhToan == true ? 'Thanh toán online' : 'Thanh toán khi nhận hàng' }</td>
										<td><fmt:formatNumber pattern="#,##0 'VNĐ'" value="${invoice.tongTien + invoice.phiVanChuyen }" /></td>
										<td> <a href="/adminIndex/quan-ly-hoa-don/${invoice.maHoaDon}"><i class="fa-solid fa-pen-to-square"></i></a> </td>
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
								        <a href="/adminIndex/quan-ly-hoa-don?pageNo=${currentPage - 1}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=${ngayFrom}&ngayTo=${ngayTo}" class="page-link" aria-label="Previous">
								            <span aria-hidden="true">«</span>
								        </a>
								    </li>
								    <c:forEach var="page" begin="1" end="${totalPages}">
								        <li class="page-item ${currentPage == page ? 'active' : ''}">
								            <a class="page-link" href="/adminIndex/quan-ly-hoa-don?pageNo=${page}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=${ngayFrom}&ngayTo=${ngayTo}">${page}</a>
								        </li>
								    </c:forEach>
								    <li class="page-item ${(totalPages == 0 || currentPage >= totalPages) ? 'disabled' : ''}">
								        <a href="/adminIndex/quan-ly-hoa-don?pageNo=${currentPage + 1}&pageSize=${pageSize}&search=${search}&sort=${sort}&ngayFrom=${#dates.format(ngayFrom, 'dd-MM-yyyy')}&ngayTo=${#dates.format(ngayTo, 'dd-MM-yyyy')}" class="page-link" aria-label="Next">
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
	        
	        if (sortSelect.value === "Ngày thanh toán") {
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
	
	<!-- SweetAlert2 JS -->
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
	
	<!-- Thêm mã JavaScript -->
	<script type="text/javascript">
	    // Hàm hiển thị thông báo thành công
	    function showSuccessAlert(message) {
	        Swal.fire({
	            icon: 'success',
	            title: 'Thành công!',
	            text: message,
	            showConfirmButton: true
	        });
	    }
	
	    // Hàm hiển thị thông báo lỗi
	    function showErrorAlert(message) {
	        Swal.fire({
	            icon: 'error',
	            title: 'Thất bại!',
	            text: message,
	            showConfirmButton: true
	        });
	    }
	</script>
	
	
<!-- 	SỬ DỤNG JSP SCRIPLET ĐỂ VIẾT ĐIỀU KIỆN CHO THÔNG BÁO -->
	<%
    String update = request.getParameter("update");
	String notFound = request.getParameter("notFound");
    if (update != null && update.equals("success")) {
	%>
	    <script>
	        // Gọi hàm hiển thị thông báo thành công
	        showSuccessAlert("Cập nhật trạng thái hóa đơn thành công!");
	    </script>
	<%
    }
    if(notFound !=null && notFound.equals("true")){
	%>
		<script type="text/javascript">
			showErrorAlert("Không tìm thấy hóa đơn yêu cầu!");
		</script>
	<%
    }
    else if(notFound !=null && notFound.equals("date")){
    	%>
    		<script type="text/javascript">
    			showErrorAlert("Ngày bắt đầu phải sớm hơn ngày kết thúc!");
    		</script>
    	<%
        }
%>


</body>
</html>

