<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
	crossorigin="anonymous" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">


</head>
<body>
	
	<link rel="stylesheet" href="/template/clients/css/shoppingCart.css">
	
	
	<div class="container-fluid"
		style="background-color: rgba(128, 128, 128, 0.055); padding: 0px; margin: 0px; margin-top: 200px">
		
		<!-- Single Page Header start -->
<div class="container-fluid page-header py-5">
    <h1 class="text-center text-white display-6">Cart</h1>
    <ol class="breadcrumb justify-content-center mb-0">
        <li class="breadcrumb-item"><a href="#">Home</a></li>
        <li class="breadcrumb-item"><a href="#">Pages</a></li>
        <li class="breadcrumb-item active text-white">Cart</li>
    </ol>
</div>
<!-- Single Page Header End -->
		<div class="row" style="margin: 50px; margin-top: 0px">
			<div class="col-md-12" style="margin-top: 30px;">
				<div class="card " style="border-radius: 0px;">
					<div class="cart-title">
						<div class="row">
							<div class="col-md-4">
								<%-- 	<input class="form-check-input" type="checkbox"
									id="checkProductAll" ${listCarts.size()==0?'disabled':''}> --%>
								<h3 style="margin-left: 50px">Sản phẩm</h3>
							</div>
							<div class="col-md-2">
								<h3>Đơn giá</h3>
							</div>
							<div class="col-md-2">
								<h3>Số lượng</h3>
							</div>
							<div class="col-md-2">
								<h3>Tiền</h3>
							</div>
							<div class="col-md-2">
								<h3>Thao tác</h3>
							</div>

						</div>
					</div>

				</div>

			</div>
			<form method="post">
				<c:forEach var="item" items="${listCarts}" varStatus="index">
					<div class="col-md-12 indexCard" style="margin-top: 10px;">
<%-- 						<div class="card" style="border-radius: 0px;">
							<div class="cart-nameStore">
								<h3 style="margin-left: 50px">${item.account.shopName}</h3>
							</div>
						</div> --%>
						<div class="card" style="border-radius: 0px;">
							<div class="cart-product">
								<div class="row">
									<div class="col-md-4">
										<input class="form-check-input product" name="idCart"
											onclick="change()" value="${item.id}" type="checkbox"
											id="gridCheck"> <img src="${item.productDetail.product.image}"
											alt="">
										<div class="title-product" style="margin-left: 10px;">
											<span style="font-weight: bold;">${item.productDetail.product.name}</span>
									
										</div>
									</div>
									<div class="col-md-2" style="display: flex;">
										<h3 style="font-size: 17px; margin-left: 10px;">
											<fmt:formatNumber>${item.productDetail.price}</fmt:formatNumber>
											<sup>đ</sup>
										</h3>
									</div>

									<div class="col-md-2"
										style="display: flex; align-items: center; justify-content: center;">
										<!-- <form> -->
											<div class="btn-group me-2" role="group"
												aria-label="First group">
												<button type="submit" class="btn btn-outline-secondary"
													onclick="decreaseValue(${index.index})"
													formaction="/user/shoppingcart/quantity"
													formmethod="get" style="border-right: none">-</button>
											<input type = "number" value = "${item.id}" name = "cartId" hidden>
												<input type="number" style="width: 60px;" readonly
													class="btn-outline-secondary text-center btnradio"
													name="btnradio" id="btnradio1" min="1"
													max="${item.productDetail.quantity}" value="${item.quantity}">
												<button type="submit" class="btn btn-outline-secondary"
													formaction="/user/shoppingcart/quantity"
													formmethod="get" style="border-left: none"
													onclick="increaseValue(${item.productDetail.quantity},${index.index})">+</button>
											</div>
									<!-- 	</form> -->
									</div>

									<div class="col-md-2">
						 			<input class="priceProduct" type="number" hidden="true"
											value="${(item.productDetail.price)*item.quantity}">
										<h3
											style="font-size: 17px; margin-left: 10px; text-align: center;">
											<fmt:formatNumber>${(item.productDetail.price)*item.quantity}</fmt:formatNumber>
											<sup>đ</sup>
										</h3>
									</div>
									<div class="col-md-2"
										style="display: flex; justify-content: center;">
										<a href="/user/shoppingcart/delete/${item.id}"
											class="btn btn-danger">Xóa</a>
									</div>

								</div>
							</div>
<!-- 							<div class="card" style="border-radius: 0px;">
								<div class="voucher-cart">
									<i class="fa-solid fa-ticket" style="color: green;"></i>
									<h3>Voucher</h3>
									<a href="">Xem thêm</a>
								</div>
							</div> -->
						</div>
					</div>
				</c:forEach>

				<!-- Thanh toán -->

				<div class="col-md-12"
					style="margin-bottom: 30px; margin-top: 20px;">
					<div class="card" style="border-radius: 0px;">
						<div class="payment-cart">
							<input class="form-check-input" type="checkbox" id="clickAll"
								${listCarts.size()==0?'disabled':''}>
							<h3 class="countCart">Chọn Tất cả [2]</h3>
						
								<button type="submit" class="btn btn-danger" id="deleteAll"
									formaction="/user/shoppingcart/deleteall" formmethod="post" disabled="true">Xóa tất cả</button>
						
							<div class="payment">
								<c:set var="total" value="0" />
								<c:set var="quantity" value="0" />
								<c:forEach var="it" items="${listCarts}">
									<c:set var="total"
										value="${total+(it.quantity*(it.productDetail.price))}" />
									<c:set var="quantity" value="${quantity+it.quantity }" />
								</c:forEach>
								<h3 id="totalProduct">
									Tổng thanh toán 0 [sản phẩm] : <span><fmt:formatNumber>0</fmt:formatNumber><sup>đ</sup></span>
								</h3>
							</div>
							<div class="btn-payment">
								<button class="btn btn-success"
									formaction="/user/cart/get-cart-pay" id="cartPay"
									disabled="true">Mua hàng</button>
							</div>
						</div>
					</div>
				</div>
			</form>







		</div>
	</div>
	</div>
	<script>
		var allChecked = document.querySelectorAll(".form-check-input.product");
		document.getElementById("clickAll").onclick = click;
		/* document.getElementById("cboProduct").onclick = click; */

		function click() {
		
			if (this.checked) {
				document.getElementById("deleteAll").disabled = false;
				document.getElementById("cartPay").disabled = false;
			 
				for (var i = 0; i < allChecked.length; i++) {
					allChecked[i].checked = true;
				 	
				}
		
			} else {
				for (var i = 0; i < allChecked.length; i++) {
					allChecked[i].checked = false;
					document.getElementById("deleteAll").disabled = true;
					document.getElementById("cartPay").disabled = true;
				}
			}
			totalProduct();
			
		};

		function change() {
			totalProduct();
			for (var i = 0; i < allChecked.length; i++) {
				if (allChecked[i].checked) {
					document.getElementById("deleteAll").disabled = false;
					document.getElementById("cartPay").disabled = false;
					return;
				}
			}
			document.getElementById("deleteAll").disabled = true;
			document.getElementById("cartPay").disabled = true;
		}

		document.addEventListener('DOMContentLoaded', function() {
			// Code sẽ được thực thi khi trang web đã tải xong
			console.log('Trang web đã tải xong!');
			var cart = document.querySelectorAll(".indexCard");
			var count = document.querySelector(".countCart");
			count.innerHTML = "Chọn Tất cả [" + cart.length + "]";
		});
		
		function totalProduct() {
			var priceProduct = document.querySelectorAll(".priceProduct");
			var total = 0;
			var index =0;
			for (var i = 0; i <allChecked.length ; i++) {
				if(allChecked[i].checked){
				  index++;
				  total = total + parseFloat(priceProduct[i].value);
				}	
			}
			
			document.getElementById('totalProduct').innerHTML = "Tổng thanh toán "+index+" [sản phẩm] : "+total.toLocaleString().replace(/,/g, '.')+"<sup>đ<sup>"
			

		}
	</script>
	<script>
	//3.Tăng giảm số lượng
	function increaseValue(index,page) {
		var value = parseInt(document.querySelectorAll('.btnradio')[page].value, 10);
		value = isNaN(value) ? 0 : value;
		if (value < index) {
			value++;
		} else {
			document.querySelectorAll('.btnradio')[page].value = index;
			return;
		}
		document.querySelectorAll('.btnradio')[page].value = value;
	}

	function changeValue(index) {
		var value = parseInt(document.getElementById('btnradio1').value, 10);
		value = isNaN(value) ? 0 : value;
		if (value > index) {
			document.getElementById('btnradio1').value = index;
		}
	}

	function decreaseValue(page) {
		var value = parseInt(document.querySelectorAll('.btnradio')[page].value, 10);
		value = isNaN(value) ? 0 : value;
		if (value > 1) {
			value--;
			document.querySelectorAll('.btnradio')[page].value = value;
		}
		
	}

	</script>
</body>
</html>