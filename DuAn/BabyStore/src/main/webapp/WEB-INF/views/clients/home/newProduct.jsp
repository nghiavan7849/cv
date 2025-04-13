
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/common/taglib.jsp"%>


<!-- Vesitable Shop Start-->
<div class="container-fluid vesitable py-5">

	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@ include file="/common/taglib.jsp"%>

	<!-- Vesitable Shop Start-->
	<div class="container-fluid vesitable py-5">
		<div class="container py-5">
			<h1 class="mb-0">Latest product</h1>
			<div class="owl-carousel vegetable-carousel justify-content-center">
				<c:forEach var="item" items="${findLastestProducts}">
					<div
						class="border border-primary rounded position-relative vesitable-item">
						<div class="vesitable-img">
							<img
								src="<c:url value='/template/clients/asset/images/${item.image}'/>"
								class="img-fluid w-100 rounded-top" alt="">
						</div>
						<div
							class="text-white bg-primary px-3 py-1 rounded position-absolute"
							style="top: 10px; right: 10px;">${item.category.name}</div>
						<div class="p-4 rounded-bottom">
							<h4>${item.name}</h4>
							<p>${item.description}</p>
							<div class="d-flex justify-content-between flex-lg-wrap">
								<p class="text-dark fs-5 fw-bold mb-0">
									$
									<fmt:formatNumber value="${item.total}" />
								</p>
								<a href="#"
									class="btn border border-secondary rounded-pill px-3 text-primary"><i
									class="fa fa-shopping-bag me-2 text-primary"></i> Add to cart</a>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>

	</div>
</div>
<!-- Vesitable Shop End -->
