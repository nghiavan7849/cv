
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title></title>
	<style>
        
    </style>
</head>
<body class="body-pay-success">
<!-- Single Page Header start -->
	<div class="container-fluid page-header py-5">
	    <h1 class="text-center text-white display-6">Payment success</h1>
	    <ol class="breadcrumb justify-content-center mb-0">
	        <li class="breadcrumb-item"><a href="#">Home</a></li>
	        <li class="breadcrumb-item"><a href="#">Pages</a></li>
	        <li class="breadcrumb-item active text-white">Payment success</li>
	    </ol>
	</div>
<!-- Single Page Header End -->

	
<!-- Single Product Start -->
<div class="py-5 me-5 ms-5">
  	<div class="container mt-3 mb-5">
   	
        <h3 class="payment-header">Thanh toán thành công</h3>

        <div class="row">
            <div class="col-md-8">
                <div class="order-details">
                    <h4>CHI TIẾT ĐƠN HÀNG</h4>
                    <table class="table table-borderless">
                        <tbody>
                        	<c:forEach var='item' items="${order.orderDetails}">
	                            <tr>
	                                <td>${item.productDetail.product.name} × ${item.quantity}</td>
	                                <td class="text-right">${item.productDetail.price * item.quantity}</td>
	                            </tr>
                            </c:forEach>
                            <tr>
                                <td>Tạm tính:</td>
                                <td class="text-right"><fmt:formatNumber>${order.total}</fmt:formatNumber> đ</td>
                            </tr>
                            <tr>
                                <td>Phí vận chuyển:</td>
                                <td class="text-right"><fmt:formatNumber>${order.shippingFee}</fmt:formatNumber> đ</td>
                            </tr>
                            <tr>
                                <td>Phương thức thanh toán:</td>
                                <c:choose>
                                	<c:when test="${order.paymentMethod}">                                	
                                		<td class="text-right">Thanh toán khi nhận hàng (COD)</td>
                                	</c:when>
                                	<c:otherwise>
                                		<td class="text-right">Thanh toán online</td>
                                	</c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <td class="total-amount">Tổng cộng:</td>
                                <td class="text-right total-amount"><fmt:formatNumber>${order.total + order.shippingFee}</fmt:formatNumber> đ</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="col-md-4">
                <div class="order-summary">
                    <h4>Cảm ơn bạn. Đơn hàng của bạn đã được nhận.</h4>
                    <p><strong>Mã đơn hàng:</strong> ${order.id}</p>
                    <p><strong>Ngày:</strong> ${order.orderDate}</p>
                    <p><strong>Số điện thoại:</strong> ${order.address.numberPhone}</p>
                    <p><strong>Đia chỉ:</strong> ${order.address.fullNameAddress}</p>
                    <p><strong>Phương thức thanh toán:</strong> 
                    <c:choose>
	                    <c:when test="${order.paymentMethod}">                                	
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
</div>
<!-- Single Product End -->
</body>
</html>
