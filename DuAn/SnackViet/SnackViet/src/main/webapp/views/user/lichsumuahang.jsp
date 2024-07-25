<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Lịch Sử Mua Hàng</title>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/lichsumuahang.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>

    <!-- Menu -->
    <%@ include file="menu.jsp"%>
    <!-- End Menu -->

    <!-- Tabs for different statuses -->
   
    <hr />
	 <ul class="nav nav-tabs mb-4" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dadathang' ? 'active' : ''}" href="${request.contextPath}/lich-su-mua-hang?trangthai=dadathang">Đã Đặt Hàng</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'daxacnhan' ? 'active' : ''}" href="${request.contextPath}/lich-su-mua-hang?trangthai=daxacnhan">Đã Xác Nhận</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dangxuly' ? 'active' : ''}" href="${request.contextPath}/lich-su-mua-hang?trangthai=dangxuly">Đang Xử Lý</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dangvanchuyen' ? 'active' : ''}" href="${request.contextPath}/lich-su-mua-hang?trangthai=dangvanchuyen">Đang Vận Chuyển</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'giaothanhcong' ? 'active' : ''}" href="${request.contextPath}/lich-su-mua-hang?trangthai=giaothanhcong">Giao Thành Công</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dahuy' ? 'active' : ''}" href="${request.contextPath}/lich-su-mua-hang?trangthai=dahuy">Đã Hủy</a>
        </li>
    </ul>
	 <hr />
    <div class="container cart-container mb-5">
        <h3 class="cart-header mt-4">Hóa đơn mua hàng</h3>

        <!-- Invoice Table -->
        <div class="table-responsive mb-4">
            <table class="table">
                <thead>
                    <tr>
                        <th>Mã hóa đơn</th>
                        <th>Tổng Sản phẩm</th>
                        <th>Địa Chỉ</th>
                        <th>Trạng Thái</th>
                        <th>Tổng Tiền</th>
                        <th>Thao Tác</th>
                        <c:if test="${param.trangthai == 'dangvanchuyen'}">
 							<th></th>
 						</c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listDonHangs}">
                        <tr>
                            <td>${item.maHoaDon}</td>
                            <td>${item.tongSanPham}</td>
                            <td>${item.diaChi}</td>
                            <td style="color: red">${item.tenTrangThai}</td>
                            <td><b><fmt:formatNumber>${item.tongTienVaPhiVanChuyen}</fmt:formatNumber> VNĐ</b></td>
                            <td class="text-center">
                                <!-- Thêm các thao tác tương ứng nếu cần -->
                                <a href="${request.contextPath}/lich-su-mua-hang?${paramTrangThai}maHD=${item.maHoaDon}"> <i class="fa-solid fa-pen-to-square" style="font-size: 20px"></i></a>
                            </td>
                            <c:if test="${param.trangthai == 'dangvanchuyen'}">
                            	<td>
	                             	<a href="${request.contextPath}/lich-su-mua-hang/da-nhan-duoc-hang?idHD=${item.maHoaDon}" class="btn btn-sm btn-buy-again">Đã nhận được hàng</a>
	                            </td>             
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <br>
      	<hr />
      	<c:if test="${checkMaHD}">
	        <p class="cart-header mt-4">Chi tiết hóa đơn</p>
	        <!-- Purchased Items Table -->
	        <div class="tab-content" id="myTabContent">
	            <div class="tab-pane fade show active" id="purchased" role="tabpanel" aria-labelledby="purchased-tab">
	                <div class="table-responsive">
	                    <table class="table">
	                        <thead>
	                            <tr>
	                                <th>Mã hóa đơn</th>
	                                <th>Hình ảnh</th>
	                                <th>Sản phẩm</th>
	                                <th>Số Lượng</th>
	                                <th>Tổng Tiền</th>
	                                <th>Thao Tác</th>
	                            </tr>
	                        </thead>
	                        <tbody>
	                            <c:forEach var="item" items="${listLichSus}">
	                                <tr>
	                                    <td>${item.maHoaDon}</td>
	                                    <td><img src="${request.contextPath}/image/AnhSanPham/${item.hinhAnh}" alt="Product Image" width="50"></td>
	                                    <td>${item.tenSanPham}</td>
	                                    <td>${item.soLuong}</td>
	                                    <td><b><fmt:formatNumber>${item.tongTien}</fmt:formatNumber> VNĐ</b></td>
	                                    <td class="align-content-center">
	                                        <c:if test="${param.trangthai == 'giaothanhcong'}">
	                                            <a href="${request.contextPath}/gio-hang/mua-hang/dat-hang?idSP=${item.maSanPham}" class="btn btn-sm btn-buy-again">Mua Lại</a>
	                                            <a href="${request.contextPath}/danh-gia/${item.tenSanPham}?tongTien=${item.tongTien}&soLuong=${item.soLuong}" class="btn btn-sm btn-review">Đánh giá</a>
	                                        </c:if>
	                                    </td>
	                                </tr>
	                            </c:forEach>
	                        </tbody>
	                    </table>
	                </div>
	            </div>
	        </div>
		</c:if>
    </div>

    <!-- Footer -->
    <%@ include file="footer.jsp"%>
    <!-- End Footer -->

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
