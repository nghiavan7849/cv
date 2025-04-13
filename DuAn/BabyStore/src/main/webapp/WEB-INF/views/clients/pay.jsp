
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title></title>
	<style>
        
    </style>
</head>
<body class="body-pay">
<!-- Single Page Header start -->
	<div class="container-fluid page-header py-5">
	    <h1 class="text-center text-white display-6">Pay</h1>
	    <ol class="breadcrumb justify-content-center mb-0">
	        <li class="breadcrumb-item"><a href="#">Home</a></li>
	        <li class="breadcrumb-item"><a href="#">Pages</a></li>
	        <li class="breadcrumb-item active text-white">Pay</li>
	    </ol>
	</div>
<!-- Single Page Header End -->

	
<!-- Single Product Start -->
<div class="py-5 me-5 ms-5">
  	<div class="container mt-3 mb-5">

        <div class="row">
            <div class="col-md-8">
                <div class="order-details">
                    <h4>Thông Tin Thanh Toán</h4>
                       <c:if test="${!empty message}">
                        <span class="text-danger">${message} Tại đây</span>
                    	</c:if>
                    <form>
                        <div class="form-group mt-2">
                            <label for="email">Số điện thoại</label>
                            <input type="text" class="form-control" id="soDienThoai" value="${address.numberPhone}" placeholder="Nhập địa chỉ số điện thoại" disabled="disabled">
                        </div>
                        <div class="form-group mt-2">
                            <label for="name">Họ và Tên</label>
                            <input type="text" class="form-control" id="name" value="${address.account.fullName}" placeholder="Nhập họ và tên" disabled="disabled">
                        </div>
                        <div class="form-group mt-2">
                            <label for="address">Địa chỉ</label>
                            <input type="text" class="form-control" id="address" value="${address.fullNameAddress}" placeholder="Nhập địa chỉ" disabled="disabled">
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-md-4">
                <div class="order-summary">
                    <h4>Đơn Hàng Của Bạn</h4>
                    <form action="${request.contextPath}/user/cart/get-cart-pay/order" method="post">
                    
	                    <table class="table table-borderless">
	                        <tbody>
	                       		<c:forEach var="item" items="${listCartDetails}">
		                            <tr>
		                                <td>${item.productDetail.product.name} × ${item.quantity }</td>
		                                <td class="text-center"><fmt:formatNumber>${item.price * item.quantity}</fmt:formatNumber>  đ</td>
		                            
		                            </tr>
	                            </c:forEach>
	                            <tr>
	                                <td>Tạm tính</td>
	                                <td class="text-center"><fmt:formatNumber>${total}</fmt:formatNumber> đ</td>
	                            </tr>
	                            <tr>
	                                <td>Phí vận chuyển</td>
	                                <td class="text-center"><span id="phi-van-chuyen"></span> đ</td>
	                            </tr>
	                            <tr>
	                                <td class="total-amount">Tổng</td>
	                                <td class="text-center total-amount"><span id="tong-tien"></span> đ</td>
	                            </tr>
	                        </tbody>
	                    </table>
	                    <div class="form-check">
	                        <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="cod" checked>
	                        <label class="form-check-label" for="cod">
	                            Thanh toán khi nhận hàng (COD)
	                        </label>
	                    </div>
	                    <div class="form-check">
	                        <input class="form-check-input" type="radio" name="paymentMethod" id="online" value="online">
	                        <label class="form-check-label" for="online">
	                            Thanh toán online
	                        </label>
	                    </div>
	                    
	                    <c:forEach var="item" items="${listCartDetails}">
	                    	<input type="hidden" value="${item.id}" name="idCart">
	                    </c:forEach>
						<input type="hidden" id="phiVanChuyen" name="phiVanChuyen">
						<input type="hidden" id="total" name="total">
	                    <button type="submit" class="btn btn-success btn-block mt-4" id="xac-nhan-dat-hang" >Đặt Hàng</button>
	                    <p class="note mt-3">Bằng cách nhấp vào Đặt hàng, bạn đồng ý với <a href="#">điều khoản và điều kiện</a> và <a href="#">chính sách riêng tư</a> của chúng tôi.</p>
                	</form>	
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Single Product End -->
<script type="text/javascript">
		document.getElementById('xac-nhan-dat-hang').setAttribute('disabled','disabled');
		let url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee';
		let token = 'bab9fb8c-2323-11ef-a951-76299e96dead';
		let shopId = '192476';
		
		let districtCode = '${address.districtCode}';
		let wardCode = '${address.wardCode}';
		let totalWeight = '${totalWeight}';
		
		fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services', {
		    method: 'POST',
		    headers: {
		        'Content-Type': 'application/json',
		        'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead'
		    },
		    body: JSON.stringify({
		        shop_id: 192476, // Thay bằng shop_id của bạn nếu cần
		        from_district: 1574,
		        to_district: parseInt(districtCode)
		    })
		})
		.then(response => response.json())
		.then(data => {
			const dataFee = {
				    from_district_id: 1574,
				    from_ward_code: '550307',
				    service_id: parseInt(data.data[0].service_id),
				    service_type_id: parseInt(data.data[0].service_type_id),
				    to_district_id: parseInt(districtCode),
				    to_ward_code: wardCode,
				    weight: parseInt(totalWeight),
				};

				const headers = {
				    'Content-Type': 'application/json',
				    'Token': token,
				    'ShopId': parseInt(shopId)
				};

				// URL API tính phí vận chuyển
				const url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee';

				fetch(url, {
				    method: 'POST',
				    headers: headers,
				    body: JSON.stringify(dataFee)
				})
				.then(response => {
				    if (!response.ok) {
				        return response.json().then(err => {
				            throw new Error(`Error: ${err.message} (code: ${err.code})`);
				        });
				    }
				    return response.json();
				})
				.then(data => {
				    console.log(data);

				    if (data.code !== 200 || !data.data) {
				        throw new Error(`Error: ${data.message} (code: ${data.code})`);
				    }

				    const phiVanChuyen = data.data.total;
				    let total = '${total}';
				    document.getElementById('phi-van-chuyen').innerText = parseFloat(phiVanChuyen).toLocaleString('vi-VN');
				    document.getElementById('phiVanChuyen').value = phiVanChuyen;
				    document.getElementById('total').value = total;
				   
						
				    let spanTongTien = parseFloat(total) + parseFloat(phiVanChuyen);
				    console.log(spanTongTien);
				    document.getElementById('tong-tien').innerText = parseFloat(spanTongTien).toLocaleString('vi-VN');
				    document.getElementById('xac-nhan-dat-hang').removeAttribute('disabled');
				})
				.catch(error => {
				    console.error('Error:', error);
				});

		})
		.catch(error => {
		    console.error('Error fetching available services:', error);
		});
</script>

</body>
</html>
