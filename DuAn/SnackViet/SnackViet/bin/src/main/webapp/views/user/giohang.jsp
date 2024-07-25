<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Giỏ hàng</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th><input type="checkbox"></th>
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
	                        <td class="align-content-center"><input type="checkbox"></td>
	                        <td class="align-content-center">
	                            <div class="product-info">
	                                <img src="${request.contextPath}/image/AnhSanPham/${ctgh.sanPhamGH.listHinhAnhSP[0].tenHinhAnh}" alt="Product Image">
	                                Bánh tráng
	                            </div>
	                        </td>
	                        <td class="align-content-center"><fmt:formatNumber>${ctgh.sanPhamGH.gia}</fmt:formatNumber> đ</td>
	                        <td class="align-content-center">
	                            <div class="quantity-control">
	                                <button class="btn btn-sm">-</button>
	                                <input type="number" class="form-control" value="${ctgh.soLuong}" min="1">
	                                <button class="btn btn-sm">+</button>
	                            </div>
	                        </td>
	                        <td class="align-content-center"><fmt:formatNumber>${ctgh.sanPhamGH.gia * ctgh.soLuong}</fmt:formatNumber> đ</td>
	                        <td class="align-content-center "><a href="${request.contextPath}/gio-hang/remove/${ctgh.maChiTietGioHang}" class="remove-link d-flex justify-content-center"><i class="fa-solid fa-trash-can " style="color: #ff0000; font-size:22px;"></i></a></td>
	                    </tr>
	                </c:forEach>
                    <!-- Repeat for other items -->
                </tbody>
            </table>
        </div>
        <div class="cart-footer row">
        	 <table style="width: 1140px; padding: 0 20px;">   
                <tbody>
                    <tr class="row">
                        <td class="col-md-3 align-content-center"><input type="checkbox"> Chọn Tất Cả (${listCTGH.size()})</td> 
                        <td class="col-md-3 align-content-center"><a href="" class="remove-link">Xóa</a></td> 
                        <td class="col-md-3 align-content-center">Tổng thanh toán (0 sản phẩm): <span class="total-amount"> 0đ </span> </td> 
                        <td class="col-md-3 align-content-center d-flex justify-content-center"><button class="btn btn-success">Mua Hàng</button></td> 
                    </tr>
                </tbody>
            </table> 
        </div>
    </div>
    
     <!-- Footer -->
    <%@ include file="footer.jsp" %>
    <!-- End Footer -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>