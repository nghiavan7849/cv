<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
	
	<!-- SweetAlert2 CSS -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@10"></link>
	
	<style>
		#pass{
			border: none;
	        background: none;
	        color: black;
		}
		#pass:focus{
			outline: none;
		}
	</style>
	
<body>
	<div class="wrapper">

		<%@ include file="templates/sideNav.jsp"%>

		<div class="main">

			<%@ include file="templates/horizontalNav.jsp"%>

			<main class="content">
				<div class="container-fluid p-0">

					<h1 class="h3 mb-3">Danh sách người dùng</h1>

					<div class="row">
						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col mt-0">
											<h5 class="card-title">Tổng số người dùng</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-arrow-up-right-dots"></i>
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
											<h5 class="card-title">Số người dùng bị khóa</h5>
										</div>

										<div class="col-auto">
											<div class="stat text-primary">
												<i class="fa-solid fa-arrow-up-right-dots"></i>
											</div>
										</div>
									</div>
									<h1 class="mt-1 mb-3">
									
										${fn:length(inactive) }
									
									</h1>
<!-- 									<div class="mb-0"> -->
<!-- 										<span class="text-danger fw-bold"> <i class="mdi mdi-arrow-bottom-right"></i> 16% -->
<!-- 										</span> <span class="text-muted">Tăng trong tháng</span> -->
<!-- 									</div> -->
								</div>
							</div>
						</div>
					</div>
					
					<div class="userDetail" style="margin-top:20px;">
						<h4> Chi tiết người dùng </h4>
						
						<form method="post" enctype="multipart/form-data">
							  <div class="row">
							  	<input type="text" readonly hidden name="idForUpdate" value="${idForUpdate }">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Họ và tên:</label>
							  			<input value="${displayU.hoVaTen }" name="hoVaTen" type="text" class="form-control" placeholder="Nhập vào họ và tên">
							  			<em class="text-danger">${errFullname }</em>
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Tên tài khoản:</label>
							  			<input id="tenDangNhap" ${editting == true ? 'disabled' : '' } name="tenDangNhap" value="${displayU.tenDangNhap }" type="text" class="form-control"  placeholder="Nhập vào tên tài khoản">
							  			<em class="text-danger">${errUsername }</em>
							  		</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Mật khẩu:</label>
							  			<input id="matKhau" ${editting == true ? 'disabled' : '' } name="matKhau" value="${displayU.matKhau }" type="password" class="form-control" placeholder="Nhập vào mật khẩu">
							  			<em class="text-danger">${errPass }</em>
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Email:</label>
							  			<input id="email" ${editting == true ? 'disabled' : '' } name="email" value="${displayU.email }" type="email" class="form-control" placeholder="Nhập vào email">
							  			<em class="text-danger">${errEmail }</em>
							  		</div>
							  	</div>
							  </div>
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Vai trò:</label>
							  			<div class="form-check">
										    <input ${editting == true ? (displayU.vaiTro == true ? 'checked' : '') : 'checked'} class="form-check-input" value="Admin" type="radio" name="vaiTro" id="flexRadioDefault1">
										    <label class="form-check-label" for="flexRadioDefault1">
										        Admin
										    </label>
										</div>
										<div class="form-check">
										    <input ${editting == true ? (displayU.vaiTro == false ? 'checked' : '') : 'checked'} class="form-check-input" value="User" type="radio" name="vaiTro" id="flexRadioDefault2">
										    <label class="form-check-label" for="flexRadioDefault2">
										        User
										    </label>
										</div>							  		
									</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Trạng thái:</label>
							  			<div class="form-check">
										  <input ${editting == true ? (displayU.trangThai == true ? 'checked' : '') : 'checked'} class="form-check-input" type="radio" value="Hoạt động" name="trangThai" id="flexRadioDefault3">Hoạt động
										</div>
										<div class="form-check">
										  <input ${editting == true ? (displayU.trangThai == false ? 'checked' : '') : ''} class="form-check-input" type="radio" value="Bị khóa" name="trangThai" id="flexRadioDefault4">Bị khóa
										</div>
							  		</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Số điện thoại:</label>
							  			<input id="soDienThoai" ${editting == true ? 'disabled' : '' } value="${displayU.soDienThoai }" name="soDienThoai" type="text" class="form-control">
							  			<em class="text-danger">${errPhone }</em>
							  		</div>
							  	</div>
							  	<div class="col-md-6">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Giới tính:</label>
							  			<div class="form-check">
										    <input ${editting == true ? (displayU.gioiTinh == true ? 'checked' : '') : 'checked'} class="form-check-input" value="Nam" type="radio" name="gioiTinh" id="flexRadioDefault5">
										    <label class="form-check-label" for="flexRadioDefault5">
										        Nam
										    </label>
										</div>
										<div class="form-check">
										    <input ${editting == true ? (displayU.gioiTinh == false ? 'checked' : '') : 'checked'} class="form-check-input" value="Nữ" type="radio" name="gioiTinh" id="flexRadioDefault6">
										    <label class="form-check-label" for="flexRadioDefault6">
										        Nữ
										    </label>
										</div>							  		
									</div>
							  	</div>
							  </div>
							  
							  <div class="row">
							  	<div class="col-md-12">
							  		<div class="mb-3">
							  			<label class="form-label fw-bold">Chọn ảnh:</label>
							  			<input name="hinhAnh" type="file" class="form-control">
<%-- 							  			<em class="text-danger">${errImg }</em> --%>
							  		</div>
							  	</div>
							  </div>
							   
							  <button ${editting == true ? 'disabled' : '' } formaction="/adminIndex/quan-ly-nguoi-dung/insert" class="btn btn-info">Thêm</button>
							  <button onclick="enableFields()" ${editting == false ? 'disabled' : '' } formaction="/adminIndex/quan-ly-nguoi-dung/update/${displayU.maTaiKhoan }" class="btn btn-info">Cập nhật</button>
							  <button formaction="/adminIndex/quan-ly-nguoi-dung/cancel" class="btn btn-info">Làm mới</button>
						</form>
					</div>
					
					<div class="userList" style="margin-top:20px;">
						<h4 class="text-center"> Danh sách người dùng </h4>
						<form method="get" action="/adminIndex/quan-ly-nguoi-dung">
							<div class="row">
								<div class="col-md-4">
									<div class="input-group mb-3">
									  <select name="sort" class="form-select" aria-label="Default select example">
									  	  <option ${not empty displaySelected ? '' : 'disabled'} selected hidden >${not empty displaySelected ? displaySelected : 'Chọn bộ lọc' }</option>
										  <option value="Tên tài khoản">Tên tài khoản</option>
										  <option value="Số điện thoại">Số điện thoại</option>
										  <option value="Email">Email</option>
										  <option value="Họ và tên">Họ và tên</option>
									  </select>
									  <input value="${param.search }" name="search" type="search" class="form-control" placeholder="Tìm kiếm" aria-describedby="basic-addon2">
									  <button type="submit" class="btn btn-success">Tìm kiếm</button>
									</div>
								</div>
							</div>
						</form>
						
						<table class="table table-striped table-hover table-light">
							<thead class="table-warning text-center">
								<th>Mã tài khoản</th>
								<th>Tên tài khoản</th>
								<th>Mật khẩu</th>
								<th>Họ và tên</th>
								<th>Email</th>
								<th>Số ĐT</th>
								<th>Vai trò</th>
								<th>Trạng thái</th>
								<th>Giới tính</th>
								<th>Ảnh</th>
								<th>Hành động</th>
							</thead>
							
							<tbody class="text-center">
								<c:forEach items="${users }" var="u">
									<tr>
										<td>${u.maTaiKhoan }</td>
										<td>${u.tenDangNhap }</td>
										<td> <input class="text-center" id="pass" readonly="readonly" type="password" value="${u.matKhau }"> </td>
										<td>${u.hoVaTen }</td>
										<td>${u.email }</td>
										<td>${u.soDienThoai }</td>
										<td>${u.vaiTro == true ? 'Admin' : 'Khách hàng' }</td>
										<td><span class="badge ${u.trangThai == true ? 'bg-success' : 'bg-danger' }">${u.trangThai == true ? 'Hoạt động' : 'Bị khóa' }</span></td>
										<td>${u.gioiTinh == true ? 'Nam' : 'Nữ' }</td>
										<td> <img alt="" src="/image/avatars/${u.hinhAnh }" style="width: 15%"> </td>
										<td> <a href="/adminIndex/quan-ly-nguoi-dung/${u.maTaiKhoan}?pageNo=${page}&pageSize=${pageSize}&search=${search}&sort=${sort}"><i class="fa-solid fa-pen-to-square"></i></a> </td>
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
							            <a href="/adminIndex/quan-ly-nguoi-dung?pageNo=${currentPage - 1}&pageSize=${pageSize}&search=${search}&sort=${sort}" class="page-link" aria-label="Previous">
							                <span aria-hidden="true">«</span>
							            </a>
							        </li>
							        <c:forEach var="page" begin="1" end="${totalPages}">
							            <li class="page-item ${currentPage == page ? 'active' : ''}">
							                <a class="page-link" href="/adminIndex/quan-ly-nguoi-dung?pageNo=${page}&pageSize=${pageSize}&search=${search}&sort=${sort}">${page}</a>
							            </li>
							        </c:forEach>
							        <li class="page-item ${(totalPages == 0 || currentPage >= totalPages) ? 'disabled' : ''}">
							            <a href="/adminIndex/quan-ly-nguoi-dung?pageNo=${currentPage + 1}&pageSize=${pageSize}&search=${search}&sort=${sort}" class="page-link" aria-label="Next">
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
	String insert = request.getParameter("insert");
	String notFound = request.getParameter("notFound");
	
	String cause = request.getParameter("cause");
	
// 	HIỂN THỊ LỖI KHI INSERT TRÙNG CÁC TRƯỜNG DỮ LIỆU UNIQUE
    if(insert!=null && insert.equals("success")){
	%>
		<script type="text/javascript">
			showSuccessAlert("Thêm người dùng thành công")
		</script>
	<%
    }
    else if(insert!=null && insert.equals("fail") && cause.equals("tenDangNhap")){
    %>	
    	<script type="text/javascript">
			showErrorAlert("Tên đăng nhập đã tồn tại!");
		</script>
    <%
    }
    else if(insert!=null && insert.equals("fail") && cause.equals("email")){
    %>
    	<script type="text/javascript">
    		showErrorAlert("Email đã tồn tại!");
    	</script>
    <%	
    }
    else if(insert!=null && insert.equals("fail") && cause.equals("soDienThoai")){
        %>
        	<script type="text/javascript">
        		showErrorAlert("Số điện thoại đã tồn tại!");
        	</script>
        <%	
    }
// 	HIỂN THỊ LỖI KHI UPDATE TRÙNG CÁC TRƯỜNG DỮ LIỆU UNIQUE
    if (update != null && update.equals("success")) {
    	%>
    	    <script>
    	        // Gọi hàm hiển thị thông báo thành công
    	        showSuccessAlert("Cập nhật tài khoản thành công!");
    	    </script>
    	<%
    }
    else if(update!=null && update.equals("fail") && cause.equals("tenDangNhap")){
        %>	
        	<script type="text/javascript">
    			showErrorAlert("Tên đăng nhập đã tồn tại!");
    		</script>
        <%
    }
    else if(update!=null && update.equals("fail") && cause.equals("email")){
        %>
        	<script type="text/javascript">
        		showErrorAlert("Email đã tồn tại!");
        	</script>
        <%	
    }
    else if(update!=null && update.equals("fail") && cause.equals("soDienThoai")){
        %>
            <script type="text/javascript">
            	showErrorAlert("Số điện thoại đã tồn tại!");
            </script>
        <%	
    }
// 	HIỂN THỊ LỖI KHI TÌM KHÔNG THẤY NGƯỜI DÙNG
    if(notFound !=null && notFound.equals("true")){
	%>
		<script type="text/javascript">
			showErrorAlert("Không tìm thấy người dùng!");
		</script>
	<%
    }
%>


<!-- 	DÙNG ĐỂ ENABLE CÁC TRƯỜNG NGAY SAU KHI ẤN NÚT ĐĂNG NHẬP ĐỂ CÓ THỂ LẤY ĐƯỢC DỮ LIỆU -->
	<script>
		function enableFields() {
		    document.getElementById("matKhau").disabled = false;
		    document.getElementById("tenDangNhap").disabled = false;
		    document.getElementById("email").disabled = false;
		    document.getElementById("soDienThoai").disabled = false;
		}
	</script>


</body>
</html>

