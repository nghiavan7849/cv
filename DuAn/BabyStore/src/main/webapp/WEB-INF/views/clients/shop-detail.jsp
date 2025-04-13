
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<!-- Single Page Header start -->
<div class="container-fluid page-header py-5">
    <h1 class="text-center text-white display-6">Shop Detail</h1>
    <ol class="breadcrumb justify-content-center mb-0">
        <li class="breadcrumb-item"><a href="#">Home</a></li>
        <li class="breadcrumb-item"><a href="#">Pages</a></li>
        <li class="breadcrumb-item active text-white">Shop Detail</li>
    </ol>
</div>
<!-- Single Page Header End -->


<!-- Single Product Start -->
<div class="container-fluid py-5 mt-5">
    <div class="container py-5">
        <div class="row g-4 mb-5">
            <div class="col-lg-12">
                <div class="row g-4">
                    <div class="col-lg-5">
                        <div class="border rounded">
                            
                                <img src="<c:url value='/template/clients/asset/images/${product.image}'/>" style="width: 100%; height: 420px; " class="img-fluid rounded" id="main-image" alt="MainImage">
                            
                        </div>
                        <div class="image-details  row">
                            <div class="owl-carousel vegetable-carousel justify-content-center">
                                <div class="rounded position-relative vesitable-item1">
                            <a  class="col-lg-3">
                                <img src="<c:url value='/template/clients/asset/images/${product.image}'/>" class="img-fluid rounded thumbnail" alt="Thumbnail 1">
                            </a>
                            <c:forEach var="productDT" items="${product.productDetails}">
                                <c:forEach var="img" items="${productDT.imageProduct}">
                                    
                                <a  class="col-lg-3">
                                    <img src="<c:url value='/template/clients/asset/images/${img.name}'/>" class="img-fluid rounded thumbnail" alt="Thumbnail 1">
                                </a>
                           
                            </c:forEach>
                            </c:forEach>
                           </div>
                           </div>
                        </div>
                    </div>
                    <div class="col-lg-7">
                        <h4 class="fw-bold mb-4">${product.name}</h4>
                        <p class="mb-3">Category: ${product.category.name}</p>
                        <h5 class="fw-bold mb-4"><fmt:formatNumber value="${product.total}"/> đ</h5>
                        <p class="mb-4 Description">${product.description}</p>
                        <div class="mb-4 row ">
                            <h6 class="fw-bold">Color:</h6>
                            <c:forEach var="color" items="${product.productDetails}">
                            <div class="col-md-1" style="margin-right: 20px;">
                                <input type="radio" class="me-2" id="${color.color.id}" data-id="${color.id}" style="display: none;"  name="Color" required
                                       value="Beverages">
                                <label for="${color.color.id}" style="min-width: 60px;"  class="btn btn-light colorBtn">${color.color.name}</label>
                            </div>
                            
                        </c:forEach>
                        </div>
                        <div class="input-group quantity mb-4" style="width: 25%;">
                            <div class="input-group-btn">
                                <button class="btn btn-sm btn-minus rounded-circle bg-light border" >
                                    <i class="fa fa-minus" style="font-size: 17px;"></i>
                                </button>
                            </div>
                            <input type="number" id="quantity" class="form-control form-control-sm text-center border-0" style="width: 60px;" value="1" min="1">
                            <div class="input-group-btn">
                                <button class="btn btn-sm btn-plus rounded-circle bg-light border">
                                    <i class="fa fa-plus" style="font-size: 17px;"></i>
                                </button>
                            </div>
                        </div>
                        <a id="addCart" class="btn border border-secondary rounded-pill px-4 py-2 mb-4 text-primary"><i class="fa fa-shopping-bag me-2 text-primary"></i> Add to cart</a>
                    </div>
                    <div class="col-lg-12">
                        <nav>
                            <div class="nav nav-tabs mb-3">
                                <button class="nav-link active border-white border-bottom-0" type="button" role="tab"
                                        id="nav-about-tab" data-bs-toggle="tab" data-bs-target="#nav-about"
                                        aria-controls="nav-about" aria-selected="true">Description</button>
                            </div>
                        </nav>
                        <div class="tab-content mb-5">
                            <div class="tab-pane active" id="nav-about" role="tabpanel" aria-labelledby="nav-about-tab">
                                <p>${product.description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <h1 class="fw-bold mb-0">Related products</h1>
        <div class="vesitable">
           
            <div class="owl-carousel vegetable-carousel justify-content-center">
                <c:forEach var="item" items="${listProductCategory}">
                <div class="border border-primary rounded position-relative vesitable-item1">
                    
                    <div class="vesitable-img">
                        <img src="<c:url value='/template/clients/asset/images/${item.image}'/>" class="img-fluid w-100 rounded-top" alt="">
                    </div>
                    <div class="text-white bg-primary px-3 py-1 rounded position-absolute" style="top: 10px; right: 10px;">${item.category.name}</div>
                    <div class="p-4 pb-0 rounded-bottom">
                        <h4>${item.name}</h4>
                        <p class="descriptionCT">${item.description}</p>
                        <div class="d-flex justify-content-between flex-lg-wrap">
                            <p class="text-dark fs-5 fw-bold"><fmt:formatNumber value="${item.total}"/> đ</p>
                            <a href="/user/shop-detail?id=${item.id}" class="btn border border-secondary rounded-pill px-3 py-1 mb-4 text-primary"><i class="fa fa-shopping-bag me-2 text-primary"></i>View</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            </div>
        
        </div>
    </div>
</div>
<!-- Single Product End -->
 <script>
    let idProductDetail;
    document.addEventListener('DOMContentLoaded', function() {
    // Get all radio buttons
    const radios = document.querySelectorAll('input[name="Color"]');
    
    // Add event listeners to all radio buttons
    radios.forEach(radio => {
        radio.addEventListener('change', function() {
            idProductDetail = radio.getAttribute('data-id');
            document.querySelectorAll('label.colorBtn').forEach(label => {
                label.classList.remove('btn-success');
                label.classList.add('btn-light');
            });
            this.nextElementSibling.classList.add('btn-success');
            this.nextElementSibling.classList.remove('btn-light');
            console.log("dataid" + idProductDetail);
        });
    });
    
    // Click the first radio button when the page loads
    if (radios.length > 0) {
        radios[0].click();
    }
});
        document.querySelectorAll('.thumbnail').forEach(thumbnail => {
            thumbnail.addEventListener('click', function(event) {
                event.preventDefault();
                document.getElementById('main-image').src = this.src;
            });
        });

        let paragraph = document.querySelector('.Description').innerText;


        if (paragraph.length > 182) {
        let shortenedParagraph = paragraph.slice(0, 182) + ' ...';
        document.querySelector('.Description').innerText = shortenedParagraph;
        } else {
        document.querySelector('.Description').innerText = paragraph;
    }

    let paragraphCT = document.querySelector('.descriptionCT').innerText;


if (paragraphCT.length > 57) {
let shortenedParagraph = paragraphCT.slice(0, 57) + ' ...';
document.querySelector('.descriptionCT').innerText = shortenedParagraph;
} else {
document.querySelector('.descriptionCT').innerText = paragraphCT;
}
        
    document.getElementById('addCart').addEventListener('click', (event) =>{
        window.location.href ='/user/shop/add-cart/'+idProductDetail+'/'+document.getElementById('quantity').value;
    });
 </script>
</body>
</html>
