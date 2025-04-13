<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title>
        Website Baby Store
    </title>
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

    <%-- Bootstrap Icon--%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

</head>
<body>
<header>
    <%@ include file="/common/clients/header.jsp" %>
</header>
<div>
    <sitemesh:write property='body'/>
</div>
<footer>
    <%@ include file="/common/clients/footer.jsp" %>
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
<script src="<c:url value="/template/clients/js/main2.js"/>"></script>
<%--JQuery--%>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.isotope/3.0.6/isotope.pkgd.min.js"></script>

</body>
</html>
