<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Hero Section -->
<section class="hero d-flex align-items-center">
	<div class="container">
		<div class="row">
			<div class="col-md-6">
				<h1>
					Món Ăn Vặt Tốt Nhất Dành <a style="color: #FAA41A;">Cho Bạn</a>
				</h1>
				<p>Bạn có thể dễ dàng đặt hàng tại đây rất dễ dàng và đơn giản.</p>
				<a href="#" class="btn btn-primary">Đặt Hàng Ngay</a>
			</div>
			<div class="col-md-6 text-center">
				<img src="image/chip.png" alt="Chips" class="hero-img">
			</div>
		</div>
	</div>
</section>

<!-- Services Section -->
<section class="services py-5 text-center">
	<div class="container">
		<h3 class="pb-3" style="color: red;">Nó Hoạt Động Như Thế Nào</h3>
		<h2 class="pb-3" style>Dịch Vụ Của Chúng Tôi Gồm</h2>
		<h5 style="width: 1100px; text-align: center;">Chất lượng sản
			phẩm là ưu tiên hàng đầu của chúng tôi và luôn được đảm bảo đạt tiêu
			chuẩn Halal, an toàn cho đến khi đến tay bạn.</h5>
		<br>
		<div class="row">
			<div class="col-md-4">
				<img
					src="https://img.icons8.com/?size=100&id=InBFaIcuKzYq&format=png&color=000000"
					alt="Order">
				<h4 class="my-3">Đặt Hàng Một Cách Dễ Dàng</h4>
				<p>Bạn chỉ cần đặt hàng qua ứng dụng</p>
			</div>
			<div class="col-md-4">
				<img
					src="https://img.icons8.com/?size=100&id=8HsozTgMRBk2&format=png&color=000000"
					alt="Delivery">
				<h4 class="my-3">Giao Hàng Nhanh Nhất</h4>
				<p>Giao hàng luôn đúng giờ</p>
			</div>
			<div class="col-md-4">
				<img
					src="https://img.icons8.com/?size=100&id=N2betboMPKre&format=png&color=000000"
					alt="Quality">
				<h4 class="my-3">Chất Lượng Tốt Nhất</h4>
				<p>Món ăn chất lượng nhất dành cho bạn</p>
			</div>
		</div>
	</div>
</section>

<!-- Menu Section -->
<div class="similar-product row container-center">
	<div class="col-md-3">
		<div class="card">
			<a href="${request.contextPath}/chi-tiet-san-pham"> <img
				src="/image/img.png" class="card-img-top" alt="...">
			</a>
			<div class="card-body">
				<h3 class="product-name">Snack Mực cay</h3>
				<span class="product-price">10.000 VND </span>
				<button class="btn btn-success"
					style="margin: 0 0 0 45px; width: 110px;">Mua ngay</button>
			</div>
		</div>
	</div>
	<div class="col-md-3">
		<div class="card">
			<a href="${request.contextPath}/chi-tiet-san-pham"> <img
				src="/image/img.png" class="card-img-top" alt="...">
			</a>
			<div class="card-body">
				<h3 class="product-name">Snack Mực cay</h3>
				<span class="product-price">10.000 VND </span>
				<button class="btn btn-success"
					style="margin: 0 0 0 45px; width: 110px;">Mua ngay</button>
			</div>
		</div>
	</div>
	<div class="col-md-3">
		<div class="card">
			<a href="${request.contextPath}/chi-tiet-san-pham"> <img
				src="/image/img.png" class="card-img-top" alt="...">
			</a>
			<div class="card-body">
				<h3 class="product-name">Snack Mực cay</h3>
				<span class="product-price">10.000 VND </span>
				<button class="btn btn-success"
					style="margin: 0 0 0 45px; width: 110px;">Mua ngay</button>
			</div>
		</div>
	</div>
	<div class="col-md-3">
		<div class="card">
			<a href="${request.contextPath}/chi-tiet-san-pham"> <img
				src="/image/img.png" class="card-img-top" alt="...">
			</a>
			<div class="card-body">
				<h3 class="product-name">Snack Mực cay</h3>
				<span class="product-price">10.000 VND </span>
				<button class="btn btn-success"
					style="margin: 0 0 0 45px; width: 110px;">Mua ngay</button>
			</div>
		</div>
	</div>
	<!-- Repeat the above card structure for the remaining three images -->
</div>


<!-- About Us Section -->
<section id="about-us" class="about-us d-flex align-items-center">
	<div class="container">
		<h2 style="text-align: center;">Về Chúng Tôi</h2>
		<h3 style="text-align: center;">Tóm tắt nền tảng của công ty
			chúng tôi</h3>
		<br>
		<div class="row">
			<div class="col-md-6">
				<p>${description}</p>
				<c:choose>
					<c:when test="${showFullDescription}">
						<form action="home-index#about-us" method="get">
							<input type="hidden" name="showFullDescription" value="false">
							<button type="submit" class="btn btn-primary">Thu Gọn</button>
						</form>
					</c:when>
					<c:otherwise>
						<form action="home-index#about-us" method="get">
							<input type="hidden" name="showFullDescription" value="true">
							<button type="submit" class="btn btn-primary">Đọc Thêm</button>
						</form>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-md-6 text-center">
				<img src="image/about.png" alt="About Us" class="about-us-img">
			</div>
		</div>
	</div>
</section>

<!-- Contact Section -->
<section id="contact" class="contact py-5 bg-light">
    <div class="container">
        <div class="text-center mb-5">
            <h1 class="contact-title">Liên Hệ</h1>
            <p class="contact-subtitle">Nếu bạn cần giúp đỡ, chúng tôi ở đây</p>
        </div>
        <div class="row justify-content-center align-items-center">
            <div class="col-md-4 text-center text-md-left mb-4 mb-md-0">
                <h2 class="text-center" style="font-size: 50px;">Liên Hệ Ngay</h2>
                <p class="text-center">khi bạn cần giúp hổ trợ</p>
            </div>
            <div class="col-md-8">
                <form action="/send-message" method="post">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="name" name="name"
                                placeholder="Tên" required>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="fromEmail"
                                name="toEmail" placeholder="EMAIL" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <textarea class="form-control" id="content" name="content"
                            rows="4" placeholder="Tin Nhắn" required></textarea>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary">Gửi</button>
                    </div>
                </form>
                <c:if test="${not empty message}">
                    <div class="alert alert-info">${message}</div>
                </c:if>
            </div>
        </div>
    </div>
</section>

