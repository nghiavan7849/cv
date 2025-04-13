
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title></title>

</head>
<body class="order-history">
<!-- Single Page Header start -->
<div class="container-fluid page-header py-5">
    <h1 class="text-center text-white display-6">Order History</h1>
    <ol class="breadcrumb justify-content-center mb-0">
        <li class="breadcrumb-item"><a href="#">Home</a></li>
        <li class="breadcrumb-item"><a href="#">Pages</a></li>
        <li class="breadcrumb-item active text-white">Order History</li>
    </ol>
</div>
<!-- Single Page Header End -->


<!-- Single Product Start -->
<div class="py-5 me-5 ms-5">
  	<ul class="nav nav-tabs mb-4" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dadathang' || param.trangthai == null ? 'active' : ''}" href="${request.contextPath}/user/order-history?trangthai=dadathang">Đã Đặt Hàng</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'daxacnhan' ? 'active' : ''}" href="${request.contextPath}/user/order-history?trangthai=daxacnhan">Đã Xác Nhận</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dangxuly' ? 'active' : ''}" href="${request.contextPath}/user/order-history?trangthai=dangxuly">Đang Xử Lý</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dangvanchuyen' ? 'active' : ''}" href="${request.contextPath}/user/order-history?trangthai=dangvanchuyen">Đang Vận Chuyển</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'giaothanhcong' ? 'active' : ''}" href="${request.contextPath}/user/order-history?trangthai=giaothanhcong">Giao Thành Công</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${param.trangthai == 'dahuy' ? 'active' : ''}" href="${request.contextPath}/user/order-history?trangthai=dahuy">Đã Hủy</a>
        </li>
    </ul>
	 <div style="margin:1rem 51.6px;"> <hr/></div>
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
                    <c:forEach var="item" items="${listOrderHistory}">
                        <tr>
                            <td>${item[0]}</td>
                            <td>${item[1]}</td>
                            <td>${item[2]}</td>
                            <td style="color: red">${item[3]}</td>
                            <td><b><fmt:formatNumber>${item[4]}</fmt:formatNumber> VNĐ</b></td>
                            <td class="text-center">
                                <!-- Thêm các thao tác tương ứng nếu cần -->
                                <a href="${request.contextPath}/user/order-history?${paramTrangThai}maHD=${item[0]}"> <i class="fa-solid fa-pen-to-square" style="font-size: 20px"></i></a>
                            </td>
                            <c:if test="${param.trangthai == 'dangvanchuyen'}">
                            	<td>
	                             	<a href="${request.contextPath}/user/order-history/received?idHD=${item[0]}" class="btn btn-sm btn-buy-again">Đã nhận được hàng</a>
	                            </td>             
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
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
	                                <th>Màu</th>
	                                <th>Số Lượng</th>
	                                <th>Tổng Tiền</th>
	                                <th>Thao Tác</th>
	                            </tr>
	                        </thead>
	                        <tbody>
	                            <c:forEach var="item" items="${listOrderHistoryDetails}">
	                                <tr>
	                                    <td>${item[0]}</td>
	                                    <td><img src="${request.contextPath}/image/AnhSanPham/${item[1]}" alt="Product Image" width="50"></td>
	                                    <td>${item[2]}</td>
	                                    <td>${item[3]}</td>
	                                    <td>${item[4]}</td>
	                                    <td><b><fmt:formatNumber>${item[5]}</fmt:formatNumber> VNĐ</b></td>
	                                    <td class="align-content-center">
	                                        <c:if test="${param.trangthai == 'giaothanhcong'}">
	                                            <a href="" class="btn btn-sm btn-buy-again">Mua Lại</a>
	                                            <a href="" class="btn btn-sm btn-review">Đánh giá</a>
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
</div>
<!-- Single Product End -->
</body>
</html>
