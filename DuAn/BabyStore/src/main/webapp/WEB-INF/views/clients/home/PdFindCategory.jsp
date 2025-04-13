<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 6/14/2024
  Time: 11:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>


<div class="container-fluid fruite py-5">
    <div class="container py-5">
        <div class="tab-class text-center">
            <div class="row g-4">
                <div class="col-lg-4 text-start">
                    <h1>Our Organic Products</h1>
                </div>
                <div class="col-lg-8 text-end cate-list">
                    <ul class="nav nav-pills d-inline-flex text-center mb-5">
                        <li class="nav-item active" data-fillter="*">
                            <a class="d-flex m-2 py-2 bg-light rounded-pill active" data-bs-toggle="pill">
                                <span class="text-dark" style="width: 130px;">All Products</span>
                            </a>
                        </li>
                        <c:forEach var="category" items="${categories}" varStatus="loop">
                            <li class="nav-item" data-filter=".${category.name.toLowerCase()}">
                                <a class="d-flex py-2 m-2 bg-light rounded-pill" data-bs-toggle="pill">
                                    <span class="text-dark" style="width: 130px;">${category.name}</span>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="tab-content">
                <div class="row g-4">
                    <div class="col-lg-12">
                        <div class="row g-4 product-lists">
                            <%-- <c:forach> all product --%>
                            <c:forEach var="item" items="${findAll}">
                                <div class="col-md-6 col-lg-4 col-xl-3 element-item ${item.category.name.toLowerCase()}">
                                    <div class="rounded position-relative fruite-item">
                                        <div class="fruite-img">
                                            <img src="<c:url value='/template/clients/asset/images/${item.image}'/>"
                                                 class="img-fluid w-100 rounded-top" alt="">
                                        </div>
                                        <div class="text-white bg-secondary px-3 py-1 rounded position-absolute"
                                             style="top: 10px; left: 10px;">
                                            <i class="bi bi-heart-fill"></i>
                                        </div>
                                        <div class="p-4 border border-secondary border-top-0 rounded-bottom">
                                            <h4>${item.name}</h4>
                                            <p>Lorem ipsum dolor sit amet consectetur adipisicing elit sed
                                                do
                                                eiusmod te incididunt</p>
                                            <div class="d-flex justify-content-between flex-lg-wrap">
                                                <p class="text-dark fs-5 fw-bold mb-0">$<fmt:formatNumber
                                                        value="${item.total}"/></p>
                                                <a href="#"
                                                   class="btn border border-secondary rounded-pill px-3 text-primary"><i
                                                        class="fa fa-shopping-bag me-2 text-primary"></i>
                                                    Add to
                                                    cart</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <%-- Paging--%>
                    <div class="col-12">
                        <div class="pagination d-flex justify-content-center mt-5">
                            <c:if test="${currentPage > 0}">
                                <a href="?page=${currentPage - 1}&size=${pageSize}"
                                   class="rounded">&laquo;</a>
                            </c:if>
                            <c:forEach begin="0" end="${totalPages - 1}" var="i">
                                <a href="?page=${i}&size=${pageSize}"
                                   class="rounded">${i + 1}</a>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages - 1}">
                                <a href="?page=${currentPage + 1}&size=${pageSize}"
                                   class="rounded">&raquo;</a>
                            </c:if>
                        </div>
                    </div>
                    <%-- End Paging --%>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">


</script>



