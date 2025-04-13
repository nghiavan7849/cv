
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title>BabyStore - 404</title>
    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
          rel="stylesheet">

    <!-- Icon Font Stylesheet -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="<c:url value="/template/clients/lib/lightbox/css/lightbox.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/template/clients/lib/owlcarousel/assets/owl.carousel.min.css"/>" rel="stylesheet">

    <!-- Customized Bootstrap Stylesheet -->
    <link href="<c:url value="/template/clients/css/bootstrap.min.css"/>" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="<c:url value="/template/clients/css/style.css"/>" rel="stylesheet">
</head>

<body>

<%@ include file="/common/clients/header.jsp" %>

<!-- Single Page Header start -->
<div class="container-fluid page-header py-5">
    <h1 class="text-center text-white display-6">404 Error</h1>
    <ol class="breadcrumb justify-content-center mb-0">
        <li class="breadcrumb-item"><a href="#">Home</a></li>
        <li class="breadcrumb-item"><a href="#">Pages</a></li>
        <li class="breadcrumb-item active text-white">404</li>
    </ol>
</div>
<!-- Single Page Header End -->


<!-- 404 Start -->
<div class="container-fluid py-5">
    <div class="container py-5 text-center">
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <i class="bi bi-exclamation-triangle display-1 text-secondary"></i>
                <h1 class="display-1">404</h1>
                <h1 class="mb-4">Page Not Found</h1>
                <p class="mb-4">We’re sorry, the page you have looked for does not exist in our website! Maybe go to our
                    home page or try to use a search?</p>
                <a class="btn border-secondary rounded-pill py-3 px-5" href="home-page">Go Back To Home</a>
            </div>
        </div>
    </div>
</div>
<!-- 404 End -->
<%@ include file="/common/clients/footer.jsp" %>

<!-- JavaScript Libraries -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="<c:url value="/template/clients/lib/easing/easing.min.js"/>"></script>
<script src="<c:url value="/template/clients/lib/waypoints/waypoints.min.js"/>"></script>
<script src="<c:url value="/template/clients/lib/lightbox/js/lightbox.min.js"/>"></script>
<script src="<c:url value="/template/clients/lib/owlcarousel/owl.carousel.min.js"/>"></script>

<!-- Template Javascript -->
<script src="<c:url value="/template/clients/js/main.js"/>"></script>

</body>

</html>
