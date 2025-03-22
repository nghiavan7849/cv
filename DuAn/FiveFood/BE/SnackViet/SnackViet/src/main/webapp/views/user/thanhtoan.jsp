<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thanh Toán</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/menu.css">
	<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
    <style>
        body {
            background-color: #F5F0DC;
            color: #000;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .breadcrumb {
            background-color: transparent;
            padding: 0;
            margin-bottom: 0;
        }
        .breadcrumb-item + .breadcrumb-item::before {
            content: '>';
            color: #6c757d;
            padding: 0 5px;
        }
        .breadcrumb-item a {
            color: #6c757d;
            text-decoration: none;
            transition: color 0.3s;
        }
        .breadcrumb-item a:hover {
            color: #000;
            text-decoration: underline;
        }
        .breadcrumb-item.active {
            color: #000;
        }
        .payment-header {
            font-size: 1.75rem;
            margin-top: 20px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: bold;
        }
        .order-details, .order-summary {
            background-color: #fff;
            padding: 20px;
            margin-top: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .order-details:hover, .order-summary:hover {
            transform: translateY(-10px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
        .order-details h4, .order-summary h4 {
            margin-bottom: 20px;
            color: #000;
            font-weight: bold;
            text-align: center;
        }
        .order-summary p {
            margin-bottom: 10px;
        }
        .table-borderless td, .table-borderless th {
            border: 0;
        }
        .total-amount {
            font-size: 1.2em;
            font-weight: bold;
        }
        .note {
            color: #6c757d;
            text-align: center;
        }
        .order-summary {
            border-left: 5px solid #067A38;
        }
    </style>
</head>
<body>
		<!-- Menu -->
   <%@ include file="menu.jsp" %>
   <!-- End Menu -->
    <div class="container mt-3 mb-5">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Giỏ hàng</a></li>
                <li class="breadcrumb-item "><a >Chi tiết thanh toán</a></li>
                <li class="breadcrumb-item active" aria-current="page">Thanh toán thành công</li>
            </ol>
        </nav>

        <h3 class="payment-header">Thanh toán thành công</h3>

        <div class="row">
            <div class="col-md-8">
                <div class="order-details">
                    <h4>CHI TIẾT ĐƠN HÀNG</h4>
                    <table class="table table-borderless">
                        <tbody>
                        	<c:forEach var='item' items="${lisChiTietHoaDons}">
	                            <tr>
	                                <td>${item.sanPhamCT.tenSanPham} × ${item.soLuong}</td>
	                                <td class="text-right">${item.sanPhamCT.gia * item.soLuong}</td>
	                            </tr>
                            </c:forEach>
                            <tr>
                                <td>Tạm tính:</td>
                                <td class="text-right"><fmt:formatNumber>${hoaDon.tongTien}</fmt:formatNumber> đ</td>
                            </tr>
                            <tr>
                                <td>Phí vận chuyển:</td>
                                <td class="text-right"><fmt:formatNumber>${hoaDon.phiVanChuyen}</fmt:formatNumber> đ</td>
                            </tr>
                            <tr>
                                <td>Phương thức thanh toán:</td>
                                <c:choose>
                                	<c:when test="${hoaDon.phuongThucThanhToan}">                                	
                                		<td class="text-right">Thanh toán khi nhận hàng (COD)</td>
                                	</c:when>
                                	<c:otherwise>
                                		<td class="text-right">Thanh toán online</td>
                                	</c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <td class="total-amount">Tổng cộng:</td>
                                <td class="text-right total-amount"><fmt:formatNumber>${hoaDon.tongTien + hoaDon.phiVanChuyen}</fmt:formatNumber> đ</td>
                            </tr>
                        </tbody>
                    </table>
                    <p class="note">Lưu ý: ${hoaDon.ghiChu}</p>
                </div>
            </div>

            <div class="col-md-4">
                <div class="order-summary">
                    <h4>Cảm ơn bạn. Đơn hàng của bạn đã được nhận.</h4>
                    <p><strong>Mã đơn hàng:</strong> ${hoaDon.maHoaDon}</p>
                    <p><strong>Ngày:</strong> ${hoaDon.ngayThanhToan}</p>
                    <p><strong>Số điện thoại:</strong> ${diaChi.soDienThoai}</p>
                    <p><strong>Đia chỉ:</strong> ${diaChi.diaChi}</p>
                    <p><strong>Phương thức thanh toán:</strong> 
                    <c:choose>
	                    <c:when test="${hoaDon.phuongThucThanhToan}">                                	
	                      	Thanh toán khi nhận hàng (COD)
	                    </c:when>
	                    <c:otherwise>
							Thanh toán online
	                    </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
		 <!-- Footer -->
    <%@ include file="footer.jsp" %>

    <!-- End Footer -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</body>
</html>