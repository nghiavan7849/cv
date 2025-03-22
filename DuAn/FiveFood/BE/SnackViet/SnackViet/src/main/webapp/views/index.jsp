<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Snack Việt</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${request.contextPath}/css/footer.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Rancho&display=swap" rel="stylesheet">

</head>

<body>

  <!-- Menu -->
   <%@ include file="user/menu.jsp" %>
   <!-- End Menu -->
	
	<!-- Main -->
	<main>
   		<%@ include file="user/main.jsp" %>
	</main>
	<!-- End Main -->
	
    <!-- Footer -->
    <%@ include file="user/footer.jsp" %>
    <!-- End Footer -->
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>