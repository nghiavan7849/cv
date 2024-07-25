<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sản phẩm</title>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${request.contextPath}/css/sanpham.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <!-- Menu -->
    <%@ include file="menu.jsp"%>
    <!-- End Menu -->

    <div class="container product-container mt-4 mb-4">
        <div class="row">
            <div class="col-md-2">
                <div class="filters">
                    <form method="GET" action="${request.contextPath}/san-pham">
                        <div class="search-container">
                            <input type="text" class="search-input" name="search" placeholder="Tìm kiếm...">
                            <button type="submit" class="search-button">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                        <h3>Danh mục</h3>
                        <ul class="categories">
                            <li><a href="${request.contextPath}/san-pham?category=1"><i class="fas fa-cookie-bite"></i> Snack </a></li>
                            <li><a href="${request.contextPath}/san-pham?category=2"><i class="fas fa-bread-slice"></i> Bánh tráng </a></li>
                            <li><a href="${request.contextPath}/san-pham?category=3"><i class="fas fa-candy-cane"></i> Khô gà </a></li>
                            <li><a href="${request.contextPath}/san-pham?category=4"><i class="fas fa-candy-cane"></i> Kẹo </a></li>
                        </ul>

                        <h3>Giá</h3>
                        <div class="form-group">
                            <input type="number" class="form-control" name="minPrice" id="minPrice" placeholder="Min Price">
                            <input type="number" class="form-control" name="maxPrice" id="maxPrice" placeholder="Max Price">
                            <input type="range" class="form-control-range" id="priceRange" min="0" max="250000" step="1000">
                            <span id="priceRangeLabel">0 VND - 250000 VND</span>
                        </div>

                        <button type="submit" class="btn btn-primary">Lọc</button>
                    </form>
                </div>
            </div>
            <div class="col-md-10">
                <div class="sort-options">
                    <label for="sort">Sắp xếp theo: </label> 
                    <form method="GET" action="${request.contextPath}/san-pham" id="sortForm">
                        <select class="form-control d-inline w-auto" id="sort" name="sort" onchange="document.getElementById('sortForm').submit()">
                            <option value="best_selling" ${sort == 'best_selling' ? 'selected' : ''}>Bán chạy nhất</option>
                            <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                            <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                            <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                        </select>
                    </form>
                </div>
                <div class="similar-product row container-center">
                    <c:forEach items="${productPage.content}" var="product">
                        <div class="col-md-4">
                            <div class="card">
                                <a href="${request.contextPath}/chi-tiet-san-pham/${product.maSanPham}">
                                    <img src="${request.contextPath}/image/AnhSanPham/${product.hinhAnh}" class="card-img-top" alt="${product.tenSanPham}" >
                                </a>
                                <div class="card-body">
                                    <h3 class="product-name">${product.tenSanPham}</h3>
                                    <div class="action">
                                        <span class="product-price"><fmt:formatNumber>${product.gia}</fmt:formatNumber> VNĐ</span>
                                        <a class="btn btn-success" href="${request.contextPath}/gio-hang/mua-hang/dat-hang?idSP=${product.maSanPham}">Mua ngay</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="pagination justify-content-center">
                    <c:forEach items="${pageNumbers}" var="pageNumber">
                        <a class="page-item page-link ${pageNumber == productPage.number + 1 ? 'active' : ''}" href="${request.contextPath}/san-pham?page=${pageNumber - 1}&size=${pageSize}">${pageNumber}</a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <%@ include file="footer.jsp"%>
    <!-- End Footer -->

    <script>
        const priceRange = document.getElementById('priceRange');
        const priceRangeLabel = document.getElementById('priceRangeLabel');
        const minPriceInput = document.getElementById('minPrice');
        const maxPriceInput = document.getElementById('maxPrice');

        priceRange.addEventListener('input', function() {
            const value = priceRange.value;
            priceRangeLabel.textContent = `0 VND - ${value} VND`;
            maxPriceInput.value = value;
        });

        minPriceInput.addEventListener('input', function() {
            const minPrice = minPriceInput.value;
            const maxPrice = maxPriceInput.value || priceRange.max;
            priceRange.value = maxPrice;
            priceRangeLabel.textContent = `${minPrice} VND - ${maxPrice} VND`;
        });

        maxPriceInput.addEventListener('input', function() {
            const minPrice = minPriceInput.value || priceRange.min;
            const maxPrice = maxPriceInput.value;
            priceRange.value = maxPrice;
            priceRangeLabel.textContent = `${minPrice} VND - ${maxPrice} VND`;
        });
    </script>
</body>
</html>
