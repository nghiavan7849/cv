<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>   
    
<link href="https://fonts.googleapis.com/css2?family=Rancho&display=swap" rel="stylesheet">


<nav class="navbar navbar-expand-lg navbar-light">
    <a class="navbar-brand" href="${request.contextPath}/home-index" style="font-family: Rancho;">Snack Viet</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="${request.contextPath}/home-index">Trang Chủ</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${request.contextPath}/san-pham">Đồ Ăn</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" >Về Chúng Tôi</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" >Liên Hệ</a>
            </li>
        </ul>
    </div>
    <div class="navbar-nav ml-auto">
        
        <li class="nav-item cart-dropdown">
            <a class="nav-link cart-icon" href="${request.contextPath}/gio-hang"><i class="fas fa-shopping-cart"></i><span class="badge"></span></a>
            <div class="cart-dropdown-content" style="width:350px;">
                <!-- <img src="/image/box.png" alt="No Product"> -->
                <!-- <p>Chưa Có Sản Phẩm</p> -->
                <c:forEach var="item" items="${sessionScope.ListGioHang}">
	                <div class="row mt-1">
	                	<div class="col-md-2"><img alt="" src="/image/AnhSanPham/${item.sanPhamGH.hinhAnh}" height="40px" width="40px"> </div>
	                	<div class="col-md-5 align-content-center" >${item.sanPhamGH.tenSanPham}</div>
	                	<div class="col-md-5 align-content-center">${item.soLuong} x <fmt:formatNumber>${item.sanPhamGH.gia}</fmt:formatNumber> VNĐ</div>
	                </div>
                </c:forEach>
            </div>
        </li>
        <li class="nav-item cart-dropdown">
            <a class="nav-link cart-icon" href="#" id="userDropdown" role="button">
            	<c:choose>
	            	<c:when test="${empty sessionScope.sessionTaiKhoan.hinhAnh}">
	            		<i class="fas fa-user"></i>
	            	</c:when>
	            	<c:otherwise>
	     				<img class="img-menu" alt="" src="/image/avatars/${sessionScope.sessionTaiKhoan.hinhAnh}" >
	               	</c:otherwise>
                </c:choose>
            </a>
            <div class="cart-dropdown-content" aria-labelledby="userDropdown">
                <!-- Nội dung dropdown của người dùng -->
                <c:choose>
                	<c:when test="${empty sessionScope.sessionTaiKhoan}">
   		                <a class="dropdown-item" href="${request.contextPath}/dang-nhap">Đăng Nhập</a>
		                <a class="dropdown-item" href="${request.contextPath}/dang-ky">Đăng Ký</a>
		           	</c:when>
		           	<c:otherwise>
		                <a class="dropdown-item" href="${request.contextPath}/dia-chi">Địa chỉ</a>
		                <a class="dropdown-item" href="${request.contextPath}/lich-su-mua-hang">Đơn mua</a>
		                <a class="dropdown-item" href="${request.contextPath}/doi-mat-khau">Đổi mật khẩu</a>
		                <a class="dropdown-item" href="${request.contextPath}/cap-nhat-tai-khoan">Cập nhật tài khoản</a>
		                <c:if test="${sessionScope.sessionTaiKhoan.vaiTro}">
		                	<a class="dropdown-item" href="${request.contextPath}/adminIndex">Admin</a>
		                </c:if>
		                <a class="dropdown-item" href="${request.contextPath}/dang-xuat">Đăng xuất</a>
		           </c:otherwise>     
                </c:choose>
                
            </div>
        </li>
        
        
    </div>
</nav>