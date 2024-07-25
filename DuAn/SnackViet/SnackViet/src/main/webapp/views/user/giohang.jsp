<%@page import="com.snackviet.repository.ChiTietGioHangRepository"%>
<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Giỏ hàng</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/giohang.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>
	<!-- Menu -->
   <%@ include file="menu.jsp" %>
   <!-- End Menu -->
   

	<div class="container mt-4 cart-container ">
        <h2 class="text-center cart-header pb-2">Giỏ Hàng</h2>
        <c:choose>
           	<c:when test="${empty listCTGH}">
           		<div class="d-flex justify-content-center mt-5"><img alt="" src="/image/anhgiohang.png" width=130></div>
           		<div class="text-center fs-4">Chưa có sản phẩm</div>
           	</c:when>
        <c:otherwise>
           		<div class="table-responsive">  
		            <table class="table">
		                <thead>
		                    <tr>
		                        <th></th>
		                        <th>Sản phẩm</th>
		                        <th>Đơn Giá</th>
		                        <th>Số Lượng</th>
		                        <th>Số Tiền</th>
		                        <th class="d-flex justify-content-center">Thao Tác</th>
		                    </tr>
		                </thead>
		                <tbody>
		                	<c:forEach var="ctgh" items="${listCTGH}">
			                    <tr class="cart-item">
			                        <td class="align-content-center"><div class="d-flex justify-content-center"> <input type="checkbox" class="checkboxClass" value="${ctgh.maChiTietGioHang}" data-price="${ctgh.sanPhamGH.gia * ctgh.soLuong}"></div></td>
			                        <td class="align-content-center">
			                            <div class="product-info">
			                                <img src="${request.contextPath}/image/AnhSanPham/${ctgh.sanPhamGH.listHinhAnhSP[0].tenHinhAnh}" alt="Product Image">
			                               ${ctgh.sanPhamGH.tenSanPham}
			                            </div>
			                        </td>
			                        <td class="align-content-center"><fmt:formatNumber>${ctgh.sanPhamGH.gia}</fmt:formatNumber> đ</td>
			                        <td class="align-content-center">
			                        	<form method="post">
			                            	<div class="quantity-control">
				                                <button type="button" class="btn btn-sm" onclick="clickTruSL(${ctgh.maChiTietGioHang})" >-</button>
				                                <input type="number" class="form-control" name="soLuong" value="${ctgh.soLuong}" min="1" style="width:80px" id="soLuong">
				                                <button type="button"  class="btn btn-sm" onclick="clickCongSL(${ctgh.maChiTietGioHang})" >+</button>
			                                </div>
			                            </form>
			                        </td>
			                        <td class="align-content-center"><fmt:formatNumber>${ctgh.sanPhamGH.gia * ctgh.soLuong}</fmt:formatNumber> đ</td>
			                        <td class="align-content-center "><a data-bs-toggle="modal" data-bs-target="#xoaGioHang" data-name="${ctgh.sanPhamGH.tenSanPham}" data-id="${ctgh.maChiTietGioHang}" class="remove-link d-flex justify-content-center"><i class="fa-solid fa-trash-can " style="color: #ff0000; font-size:22px;"></i></a></td>
			                    </tr>
			                </c:forEach>
		                    <!-- Repeat for other items -->
		                </tbody>
		            </table>
		        </div>
		        <div class="cart-footer">
		        	 <table style="width: 100%; padding: 0 20px;">   
		                <tbody >
		                    <tr class="row" >
		                        <td class="col-md-2 align-content-center"><input class="ms-1" type="checkbox" id="checkboxAll"> Chọn Tất Cả (${listCTGH.size()})</td> 
		                        <td class="col-md-4 align-content-center"><a data-bs-toggle="modal" data-bs-target="#xoaTatCaGioHang" class="remove-link" style="text-decoration: none;">Xóa tất cả</a></td> 
		                        <td class="col-md-4 align-content-center"><div class="d-flex justify-content-end">Tổng thanh toán (<span id="tongSanPhamChon" class="ms-1 me-1">0 </span> sản phẩm): <span class="total-amount"> <span id="total" class="ms-1 me-1"></span> đ </span> </div> </td> 
		                        <td class="col-md-2 align-content-center d-flex justify-content-center"><button type="submit"  class="btn btn-success" id="muaHang">Mua Hàng</button></td> 
		                    </tr>
		                </tbody>
		            </table> 
		        </div>
           	</c:otherwise>
        </c:choose>
        
        
    </div>
    <!-- Modal -->
	<div class="modal fade" id="xoaGioHang" tabindex="-1" aria-labelledby="xoaGioHangLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h1 class="modal-title fs-5" id="xoaGioHangLabel">Thông báo</h1>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            <div class="modal-body">
	             	Bạn có muốn xóa sản phẩm <span id="valueModal"></span> ra khỏi giỏ hàng không? 
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Thoát</button>
	                <button type="button" class="btn btn-primary" id="deleteModal">Xác nhận</button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<div class="modal fade" id="xoaTatCaGioHang" tabindex="-1" aria-labelledby="xoaTatCaGioHangLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h1 class="modal-title fs-5" id="xoaTatCaGioHangLabel">Thông báo</h1>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            <div class="modal-body">
	             	Bạn có muốn xóa tất cả sản phẩm ra khỏi giỏ hàng không? 
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Thoát</button>
	                <button type="button" class="btn btn-primary" id="deleteAllModal">Xác nhận</button>
	            </div>
	        </div>
	    </div>
	</div>
     <!-- Footer -->
    <%@ include file="footer.jsp" %>

    <!-- End Footer -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    
    <script type="text/javascript">
    	//Thông báo xóa 
	    document.addEventListener('DOMContentLoaded', (event) => {
	        var modal = document.getElementById('xoaGioHang');
	        var deleteButton = document.getElementById('deleteModal');
			var deleteAllButton = document.getElementById('deleteAllModal');
			document.getElementById('total').innerText = 0;
			
	        modal.addEventListener('show.bs.modal', function (event) {
	            var triggerButton = event.relatedTarget; // Button that triggered the modal
	            var productId = triggerButton.getAttribute('data-id'); // Extract info from data-* attributes
	            var productName = triggerButton.getAttribute('data-name'); // Extract info from data-* attributes
	            
	            document.getElementById('valueModal').innerText = productName;
	            // Set the data-id attribute on the delete button
	            deleteButton.setAttribute('data-id', productId);
	        });
	
	        deleteButton.addEventListener('click', (event) => {
	            const productId = deleteButton.getAttribute('data-id');
	            console.log("Product ID:", productId);  // Debugging line
	
	            if (productId) {
	                // Perform the delete operation, for example:
	                window.location.href = '/gio-hang/remove/'+productId;
	            } else {
	                console.error("Product ID not found");
	            }
	        });
	        
	        // Xóa tât cả 
	        deleteAllButton.addEventListener('click', (event) => {
	        	window.location.href = '/gio-hang/xoa-tat-ca';
	        });
	    });
    	
    	// Checkbox chọn tất cả
	    var checkboxAll = document.getElementById("checkboxAll");
    	var tongSanPhamChon = document.getElementById("tongSanPhamChon"); 
    	var total = 0;
    	var tongSanPham = 0;
        checkboxAll.addEventListener("click",() => {
            var checkboxes = document.querySelectorAll(".checkboxClass");
            checkboxes.forEach(function(checkbox) {
                if(checkboxAll.checked){
                    checkbox.checked = true;
                } else {
                    checkbox.checked = false;
                }
            });
            
            if(checkboxAll.checked){
            	total = '${totalGioHang}';
            	tongSanPham = checkboxes.length;
            } else {
            	total = 0;
            	tongSanPham = 0;
            }
            tongSanPhamChon.innerText = tongSanPham;
            document.getElementById('total').innerText =  parseFloat(total).toLocaleString('vi-VN');
        })
        // Checkbox tự click checkbox tất cả khi có 1 sản phảma
        var checkboxes = document.querySelectorAll(".checkboxClass");
    	var total = 0;
        checkboxes.forEach((checkbox) =>{
            checkbox.addEventListener("click",()=>{
                var price = parseFloat(checkbox.getAttribute('data-price'));
                if(checkboxes.length == 1 ){
                    document.getElementById("checkboxAll").checked = checkboxes[0].checked;
                }
                checkFor = document.querySelectorAll(".checkboxClass");
	           	if(checkbox.checked == true){
	              	total += price;
	              	tongSanPham++;
	            } else {
	            	total -= price;
	            	tongSanPham--;
	            }
                if(tongSanPham == checkFor.length){
   					document.getElementById("checkboxAll").checked = true;
   					tongSanPhamChon.innerText = checkboxes.length;
                } else {
                    document.getElementById("checkboxAll").checked = false;
                    tongSanPhamChon.innerText = tongSanPham;
                }
                document.getElementById('total').innerText = total.toLocaleString('vi-VN');
            });
                
        });
        
        function clickTruSL(value){
        	window.location.href = '/gio-hang/giam-so-luong/' + value;	
        }
        function clickCongSL(value){
        	window.location.href = '/gio-hang/tang-so-luong/' + value;	
        }
        
        var muaHang = document.getElementById("muaHang");
        const listDiaChi = '${listdiaChis}';
        console.log(listDiaChi);
        muaHang.addEventListener('click', () =>{
            var idString = '';
            var checkboxes = document.querySelectorAll(".checkboxClass");
            checkboxes.forEach(checkbox => {
                if(checkbox.checked == true){
                    idString += checkbox.value + ',';
                }
            });
            
          	if(listDiaChi === 'empty'){
            	alert("Vui lòng thêm địa chỉ để mua hàng!!!");
            	return;
            } 
            if(idString == ''){
            	alert("Vui lòng tích chọn sản phẩm trong giỏ hàng cần mua!!!");
            	return;
            }
            if (idString.endsWith(',')) {
                idString = idString.substring(0, idString.length - 1);
            }
            
            window.location.href = "/gio-hang/dat-hang?idGH=" + idString;  
        });
    </script>
</body>
</html>