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
	
	<!-- SweetAlert2 CSS -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@10"></link>
<body>
	<div class="wrapper">

		<%@ include file="templates/sideNav.jsp"%>

		<div class="main">

			<%@ include file="templates/horizontalNav.jsp"%>

			<main class="content">
		            <div class="container-fluid p-0">
		                <h1 class="h3 mb-3">Thống kê dữ liệu</h1>
		                <div class="analyticsList" style="margin-top:20px;">
		                    <h4 class="text-center"> Thống kê theo bộ lọc </h4>
		                    <form method="get" action="/adminIndex/thong-ke">
		                    	<div class="row">
			                        <div class="col-md-3">
			                        	<label class="form-label">Ngày bắt đầu:</label>
			                            <input value="<fmt:formatDate value='${ngayFrom}' pattern='yyyy-MM-dd'/>" name="ngayFrom" class="form-control" type="date" placeholder="Nhập vào ngày bắt đầu">
			                        </div>
			                        <div class="col-md-3">
			                        	<label class="form-label">Ngày kết thúc:</label>
			                            <input value="<fmt:formatDate value='${ngayTo}' pattern='yyyy-MM-dd'/>" name="ngayTo" class="form-control" type="date" placeholder="Nhập vào ngày kết thúc">
			                        </div>
			                        <div class="col-md-3">
			                        	<label class="form-label">Bộ lọc:</label>
			                            <div class="input-group mb-3">
			                              <select name="select" class="form-select" aria-label="Default select example">
			                              	  <option ${not empty displaySelected ? '' : 'disabled'} selected hidden >${not empty displaySelected ? displaySelected : 'Chọn bộ lọc' }</option>
			                                  <option value="Lọc theo doanh thu">Lọc theo doanh thu</option>
			                              </select>
			                              <button id="searchButton" class="btn btn-success">Tìm kiếm</button>
			                            </div>
			                        </div>
			                    </div>
		                    </form>
		                    
		                    <c:if test="${filterType == 'doanhThu'}">
                            <table id="dataTable" class="table table-striped table-hover table-light">
                                <thead class="table-warning text-center">
                                    <tr>
                                        <th>Loại sản phẩm</th>
                                        <th>Số lượng bán</th>
                                        <th>Doanh thu</th>
                                    </tr>
                                </thead>
                                <tbody class="text-center">
                                    <c:forEach var="row" items="${revenueData}">
                                        <tr>
                                            <td>${row[0]}</td>
                                            <td>${row[1]}</td>
                                            <td><fmt:formatNumber pattern="#,##0 'VNĐ'" value="${row[2]}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <h2 class="text-primary">Tổng doanh thu: <fmt:formatNumber pattern="#,##0 'VNĐ'" value="${totalRevenue}"/></h2>
                        </c:if>

<!-- 		                    <div class="d-flex justify-content-between" style="margin-top:25px;"> -->
<!-- 		                        <p class="fw-bold">Đang xem trang 1 / 30</p> -->
<!-- 		                        <nav class="pagination-outer" aria-label="Page navigation"> -->
<!-- 		                            <ul class="pagination"> -->
<!-- 		                                <li class="page-item"> -->
<!-- 		                                    <a href="#" class="page-link" aria-label="Previous"> -->
<!-- 		                                        <span aria-hidden="true">«</span> -->
<!-- 		                                    </a> -->
<!-- 		                                </li> -->
<!-- 		                                <li class="page-item"><a class="page-link" href="#">1</a></li> -->
<!-- 		                                <li class="page-item"><a class="page-link" href="#">2</a></li> -->
<!-- 		                                <li class="page-item active"><a class="page-link" href="#">3</a></li> -->
<!-- 		                                <li class="page-item"><a class="page-link" href="#">4</a></li> -->
<!-- 		                                <li class="page-item"><a class="page-link" href="#">5</a></li> -->
<!-- 		                                <li class="page-item"> -->
<!-- 		                                    <a href="#" class="page-link" aria-label="Next"> -->
<!-- 		                                       <span aria-hidden="true">»</span> -->
<!-- 		                                    </a> -->
<!-- 		                                </li> -->
<!-- 		                            </ul> -->
<!-- 		                        </nav> -->
<!-- 		                    </div> -->
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
	String notFound = request.getParameter("notFound");
    if(notFound !=null && notFound.equals("date")){
    	%>
    		<script type="text/javascript">
    			showErrorAlert("Ngày bắt đầu phải sớm hơn ngày kết thúc!");
    		</script>
    	<%
        }
%>
		

</body>
</html>

