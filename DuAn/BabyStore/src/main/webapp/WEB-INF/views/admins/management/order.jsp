
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
	<!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="height: auto !important; min-height: 900px !important;">
    <h1 class="text-center pt-4 fw-bold">ORDER MANAGEMENT</h1> 
    <div class="row pt-4 m-0 pb-4">
    	<div class="col-md-7 mt-2">
    		<div class="card ms-4 me-3">
    			<div class="card-header text-center"><p class="m-0 fs-3 fw-bolder">Order details</p></div>
    			<div class="card-body pt-1">
    				<table class="table table-striped">
					   <thead>
						    <tr>
						      <th scope="col">Id</th>
						      <th scope="col">Image</th>
						      <th scope="col">Name</th>
						      <th scope="col">Quantity</th>
						      <th scope="col">Price</th>
						    </tr>
						  </thead>
						  <tbody>
						  	<c:forEach var="item" items="${listOrderDetails}">
							    <tr>
							      <th scope="row">${item.id}</th>
							      <td><img alt="" src="/template/admin/dist/img/${item.productDetail.product.image}" height=25 width=25> </td>
							      <td>${item.productDetail.product.name}</td>
							      <td>${item.quantity}</td>
							      <td><fmt:formatNumber> ${item.price}</fmt:formatNumber> VNĐ</td>
							    </tr>
							  </c:forEach>
						  </tbody>
					</table>
    			</div>
    		</div>
    	</div>
    	<div class="col-md-5 mt-2">
			<div class="fs-5 mb-3 fw-2 fw-semibold">Order status</div>
			<form action="${request.contextPath}/admin/order/update" method="post">
				<div class="me-4">
					<select class="form-select" aria-label="Default select example" ${editing?'':'disabled'} name="status">					 
						<c:forEach var="item" items="${listStatusOrder}">
							<option value="${item.id}">${item.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="mt-3 me-4 d-flex justify-content-start">
					<button class="btn btn-primary mt-1" ${editing?'':'disabled'}>Update status</button>
				</div>
			</form>
		</div>
		<div class="col-md-12 mt-5">
    		<div class="card ms-4 me-3">
    			<div class="card-header row m-0">
    				<div class="col-md-7 m-0 p-0 fs-3 fw-bolder">Order</div>
    				<div class="col-md-5 align-content-center">
    					<form action="${request.contextPath}/admin/order" method="get">
							<div class="row">
								<div class="col-md-4 p-1">
									<select class="form-select" aria-label="Default select example" name="sort" id="sort">
										  <option value="id" ${sort == 'id'?'selected':'selected'}>Id</option>
										  <option value="orderdate"  ${sort == 'orderdate'?'selected':''}>Order date</option>
										  <option value="status" ${sort == 'status'?'selected':''}>Status</option>
										  <option value="fullname" ${sort == 'fullname'?'selected':''}>Full name</option>
										  <option value="address" ${sort == 'address'?'selected':''}>Address</option>
									</select>
								</div>
								<div class="col-md-6 p-1"> 
									<input type="text" class="form-control" placeholder="Search..." name="search" id="search" value="${search}">
								</div>
								<div class="col-md-2 p-1">
									<button class="btn btn-primary">Search</button>
								</div>
							</div>
						</form>    				
    				</div>
    			</div>
    			<div class="card-body pt-1">
    				<table class="table table-striped">
					   <thead>
						    <tr>
						      <th class="text-center" scope="col">Id</th>
						      <th class="text-center" scope="col">Order date</th>
						      <th class="text-center" scope="col">Status</th>
						      <th class="text-center" scope="col">Full name</th>
						      <th class="text-center" scope="col">Address</th>
						      <th class="text-center" scope="col">Payment methods</th>
						      <th class="text-center" scope="col">Total product</th>
						      <th class="text-center" scope="col">Shipping Fee</th>
						      <th class="text-center" scope="col">Total</th>
						      <th class="text-center" scope="col"></th>				
						    </tr>
						  </thead>
						  <tbody>
						  	<c:forEach var="item" items="${listOrder}">
							    <tr>
							      <th class="align-content-center text-center" scope="row">${item.id}</th>
							      <td class="align-content-center text-center">${item.orderDate}</td>
							      <td class="align-content-center text-center">
							      	<button class="btn btn-sm
							      		 <c:choose>
										        <c:when test="${item.shippingStatus.name == 'Đã hủy'}">
										        bg-danger
										        </c:when>
										        <c:when test="${item.shippingStatus.name == 'Đang xử lý'}">
										                bg-warning
										        </c:when>
										      	<c:when test="${item.shippingStatus.name == 'Đang vận chuyển'}">
										           bg-info
										       </c:when>
										       <c:otherwise>
 										           bg-success 
										       </c:otherwise>
										   </c:choose>
							      	">${item.shippingStatus.name}</button>
							      </td>
							      <td class="align-content-center text-center">${item.account.fullName}</td>
							      <td class="align-content-center text-center">${item.address.fullNameAddress}</td>
							      <td class="align-content-center text-center">${item.paymentMethod?'Thanh toán khi nhận hàng':'Thanh toán online'}</td>
							      <td class="align-content-center text-center"><fmt:formatNumber>${item.shippingFee}</fmt:formatNumber> VNĐ</td>
							      <td class="align-content-center text-center"><fmt:formatNumber>${item.total}</fmt:formatNumber> VNĐ</td>
							      <td class="align-content-center text-center"><fmt:formatNumber>${item.shippingFee + item.total}</fmt:formatNumber> VNĐ</td>
							      <td class="align-content-center text-center">
								      <a href="${request.contextPath}/admin/order/order-details/${item.id}">
								      	<i class="fa-solid fa-pen-to-square"></i>
								      </a>
							      </td>
							    </tr>
						    </c:forEach>
						  </tbody>
					</table>
    			</div>
    		</div>
    		<div class="d-flex justify-content-between ms-4 me-3" style="margin-top:25px;">
			    <!-- Vị trí hiển thị số trang -->
			    <p class="fw-bold">Đang xem trang ${currentPage} / ${totalPages}</p>
			
			    <!-- Cập nhật nút phân trang -->
			    <nav class="pagination-outer" aria-label="Page navigation">
			        <ul class="pagination">
			            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
			                <a href="${request.contextPath}/admin/order?pageNo=${currentPage - 1}&search=${search}&sort=${sort}" class="page-link" aria-label="Previous">
			                    <span aria-hidden="true">«</span>
			                </a>
			            </li>
			            <c:forEach var="page" begin="1" end="${totalPages}">
			                <li class="page-item ${currentPage == page ? 'active' : ''}">
			                    <a class="page-link" href="${request.contextPath}/admin/order?pageNo=${page}&search=${search}&sort=${sort}">${page}</a>
			                </li>
			            </c:forEach>
			            <li class="page-item ${(totalPages == 0 || currentPage >= totalPages) ? 'disabled' : ''}">
			                <a href="${request.contextPath}/admin/order?pageNo=${currentPage + 1}&search=${search}&sort=${sort}" class="page-link" aria-label="Next">
			                    <span aria-hidden="true">»</span>
			                </a>
			            </li>
			        </ul>
			    </nav>
			    <p class="fw-bold">5 sản phẩm / 1 trang</p>
			</div>
		</div>
    </div>
    
    <!-- SweetAlert2 JS -->
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
    <script type="text/javascript">
    	var sort = document.getElementById('sort');
    	if(sort.value == 'id'){
    		document.getElementById('search').type = 'number';
    	}
    	sort.addEventListener('change', ()=>{
    		var input = document.getElementById('search');
    		if(sort.value == 'orderdate'){
    			input.type = 'date';
    		} else if(sort.value == 'id') {
    			input.type = 'number';
    		} else {
    			input.type = 'text';	
    			input.value = '';
    		}
    		
    	})
    	var update = '${messageUpdate}';
    	if(update != ''){
    		showSuccessAlert('Update invoice status successfully');
    	}
    	
    	// Hàm hiển thị thông báo thành công
	    function showSuccessAlert(message) {
	        Swal.fire({
	            icon: 'success',
	            title: 'Success!',
	            text: message,
	            showConfirmButton: true
	        });
	    }
	
	    // Hàm hiển thị thông báo lỗi
	    function showErrorAlert(message) {
	        Swal.fire({
	            icon: 'error',
	            title: 'Error!',
	            text: message,
	            showConfirmButton: true
	        });
	    }
    	
    </script>
    	
  </div>
  <!-- /.content-wrapper -->
</body>
</html>
