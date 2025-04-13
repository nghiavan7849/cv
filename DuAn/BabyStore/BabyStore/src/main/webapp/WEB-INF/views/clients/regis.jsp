<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>

<head>
    <title>BabyStore - Registration</title>

    <!-- Bootstrap CSS v5.2.1 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>

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

    <%-- Button 89--%>
    <link href="<c:url value="/template/clients/css/button89.css"/>" rel="stylesheet">

    <%-- Bootstrap Icons --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <%-- GSAP --%>
    <script src="https://cdn.jsdelivr.net/npm/gsap@3.12.5/dist/gsap.min.js"></script>

    <%-- JQuery --%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <style>
        .bi-exclamation-triangle {
            margin: 17px 17px 0 0 !important;
        }
    </style>

</head>

<body>
<!-- Spinner Start -->
<div id="spinner"
     class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
    <div class="spinner-grow text-primary" role="status"></div>
</div>
<!-- Spinner End -->


<!-- Navbar start -->
<div class="container-fluid border-bottom">
    <div class="container px-0">
        <nav class="navbar navbar-light bg-white navbar-expand-xl">
            <a href="home-page" class="navbar-brand">
                <h1 class="text-primary display-6">BabyStore</h1>
            </a>
            <button class="navbar-toggler py-2 px-3" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarCollapse">
                <span class="fa fa-bars text-primary"></span>
            </button>
            <div class="collapse navbar-collapse bg-white" id="navbarCollapse">
                <div class="navbar-nav mx-auto">
                </div>
                <div class="d-flex m-3 me-0">
                    <select class="form-select form-select-sm" aria-label="Default select example">
                        <option value="vietnamese">Việt Nam - Tiếng Việt</option>
                        <option value="english">International - English</option>
                    </select>
                    <i class="bi bi-globe2 fs-4 mx-2" style="margin-top: 3px"></i>
                </div>
            </div>
        </nav>
    </div>
</div>
<!-- Navbar End -->

<!-- Start Registration -->
<main>
    <div class="container-fluid py-5">
        <div class="d-flex justify-content-center">
            <div class="card" style="width: 600px;">
                <div class="card-body">
                    <h1 class="text-primary text-center">Registration</h1>
                    <form action="/registration" method="post" class="pt-5 px-2 row g-3" attributeeModel="account">

                        <div class="col-sm-6 position-relative">
                            <label for="txtFirstName" class="form-label">First name</label>
                            <input required type="text" name="firstName" id="txtFirstName" class="form-control">
                            <i
                                    class="bi bi-exclamation-triangle fs-3 text-danger position-absolute top-50 end-0 translate-middle-y d-none"></i>
                        </div>

                        <div class="col-sm-6 position-relative">
                            <label for="txtLastName" class="form-label">Last name</label>
                            <input required type="text" name="lastName" id="txtLastName" class="form-control">
                            <i
                                    class="bi bi-exclamation-triangle fs-3 text-danger position-absolute top-50 end-0 translate-middle-y d-none"></i>
                        </div>

                        <div class="col-sm-12 position-relative">
                            <label for="txtEmail" class="form-label">Email address</label>
                            <input required type="email" class="form-control" id="txtEmail"
                                   placeholder="Apartment, studio, or floor" name="email">
                            <i
                                    class="bi bi-exclamation-triangle fs-3 text-danger position-absolute top-50 end-0 translate-middle-y d-none"></i>
                        </div>


                        <div class="col-sm-12 position-relative">
                            <label for="txtPassword" class="form-label">Password</label>
                            <input required type="password" class="form-control" id="txtPassword" name="password">
                            <i
                                    class="bi bi-exclamation-triangle fs-3 text-danger position-absolute top-50 end-0 translate-middle-y d-none"></i>
                        </div>

                        <div class="d-flex justify-content-center">
                            <button class="button-89" role="button">Submit</button>
                        </div>

                        ${msg}
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
<!-- End Registration -->
<footer>
    <div class="container">
        <footer class="d-flex flex-wrap justify-content-between align-items-center py-3 my-4 border-top">
            <div class="col-md-4 d-flex align-items-center">
                <a href="/" class="mb-3 me-2 mb-md-0 text-muted text-decoration-none lh-1">
                    <i class="bi bi-bootstrap fs-3"></i>
                </a>
                <span class="text-muted">babystorecantho@gmail.com</span>
            </div>

            <ul class="nav col-md-4 justify-content-end list-unstyled d-flex">
                <li class="ms-3"><a class="text-muted" href="#"> <i class="bi bi-twitter fs-3"></i>
                </a></li>
                <li class="ms-3"><a class="text-muted" href="#">
                    <i class="bi bi-instagram fs-3"></i>
                </a></li>
                <li class="ms-3"><a class="text-muted" href="#"> <i class="bi bi-facebook fs-3"></i>
                </a></li>
            </ul>
        </footer>
    </div>
</footer>

<!-- JavaScript Libraries -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="<c:url value="/template/clients/lib/easing/easing.min.js"/>"></script>
<script src="<c:url value="/template/clients/lib/waypoints/waypoints.min.js"/>"></script>
<script src="<c:url value="/template/clients/lib/lightbox/js/lightbox.min.js"/>"></script>
<script src="<c:url value="/template/clients/lib/owlcarousel/owl.carousel.min.js"/>"></script>

<!-- Template Javascript -->
<script src="<c:url value="/template/clients/js/main.js"/>"></script>
<!-- Bootstrap JavaScript Libraries -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
        integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
        integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>


<script type="text/javascript">
    $(document).ready(function () {
        $(".form-control").blur(function () {
            var itemId = $(this).attr("id");
            if ($(this).val() === "") {
                $(this).addClass("broder border-danger");
                if (itemId === "txtFirstName") $("i").eq(1).removeClass("d-none");
                else if (itemId === "txtLastName") $("i").eq(2).removeClass("d-none");
                else if (itemId === "txtEmail") $("i").eq(3).removeClass("d-none");
                else $("i").eq(4).removeClass("d-none");
            }

            if ($(this).val() !== "") {
                $(this).removeClass("border border-danger");
                if (itemId === "txtFirstName") $("i").eq(1).addClass("d-none");
                else if (itemId === "txtLastName") $("i").eq(2).addClass("d-none");
                else if (itemId === "txtEmail") $("i").eq(3).addClass("d-none");
                else $("i").eq(4).addClass("d-none");
            }
        });
    });
</script>

</body>
</html>
