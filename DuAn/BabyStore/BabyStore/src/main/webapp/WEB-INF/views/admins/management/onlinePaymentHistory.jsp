
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>

</head>
<body class="hold-transition sidebar-mini layout-fixed">
	<!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
	<div class="row pt-4 m-0 pb-4">
    	<div class="col-md-12 mt-2">
    		<div class="card ms-4 me-3">
    			<div class="card-header text-center"><p class="m-0 fs-3 fw-bolder">Look up Transaction</p></div>
    			<div class="card-body pt-1">
    				<form action="https://sandbox.vnpayment.vn/merchantv2/Transaction/PaymentDetail/${displayT.transactionCode }.htm" method="get">
    					<div class="mb-3">
    						<label class="form-label">Search for transaction:</label>
    						<c:if test="${not empty displayT.transactionCode }">
    							<input readonly="readonly" name="transactionCode" class="form-control" value="https://sandbox.vnpayment.vn/merchantv2/Transaction/PaymentDetail/${displayT.transactionCode }.htm">
    						</c:if>
    						<c:if test="${empty displayT.transactionCode }">
    							<input readonly="readonly" name="transactionCode" class="form-control" value="">
    						</c:if>
    					</div>
    					<button class="btn btn-primary">Search</button>
    				</form>
    			</div>
    		</div>
    	</div>
    	
		<div class="col-md-12 mt-5">
    		<div class="card ms-4 me-3">
    			<div class="card-header row m-0">
    				<div class="col-md-7 m-0 p-0 fs-3 fw-bolder">Online Transactions</div>
    				<div class="col-md-5 align-content-center">
    					<form action="" method="get">
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
						      <th class="text-center" scope="col">Customer Name</th>
						      <th class="text-center" scope="col">Order Id</th>
						      <th class="text-center" scope="col">Transaction Date</th>
						      <th class="text-center" scope="col">Transaction Status</th>
						      <th class="text-center" scope="col">Bank Number</th>
						      <th class="text-center" scope="col">Transaction Code</th>
						      <th class="text-center" scope="col">Total</th>
						      <th class="text-center" scope="col"></th>				
						    </tr>
						  </thead>
						  <tbody>
						  	<c:forEach var="item" items="${listTransactions}">
							    <tr>
							      <th class="align-content-center text-center" scope="row">${item.id}</th>
							      <td class="align-content-center text-center">${item.account.fullName}</td>
							      <td class="align-content-center text-center">${item.order.id}</td>
							      <td class="align-content-center text-center">${item.transactionDate}</td>
							      <td class="align-content-center text-center">${item.transactionStatus == true ? 'Thành công' : 'Thất bại'}</td>
							      <td class="align-content-center text-center">${item.accountNumber}</td>
							      <td class="align-content-center text-center">${item.transactionCode}</td>
							      <td class="align-content-center text-center"><fmt:formatNumber>${item.amountMoney}</fmt:formatNumber> VNĐ</td>
							      <td class="align-content-center text-center">
								      <a href="${request.contextPath}/admin/online-payment-history/transaction-details/${item.id}">
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
			    Vị trí hiển thị số trang
			    <p class="fw-bold">Đang xem trang ${currentPage} / ${totalPages}</p>
			
			    Cập nhật nút phân trang
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
