<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 5/31/2024
  Time: 1:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<html>
<head>
    <title>
        Website Baby Store
    </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="<c:url value="/template/clients/css/orderhistory.css"/>">
    <link rel="stylesheet" href="<c:url value="/template/clients/css/pay.css"/>">
    <link rel="stylesheet" href="<c:url value="/template/clients/css/paysuccess.css"/>">
	
	<link rel="stylesheet" href="${request.contextPath}/css/diachi.css">
	
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

</body>
</html>
