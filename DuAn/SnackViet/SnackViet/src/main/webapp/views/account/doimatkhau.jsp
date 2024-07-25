<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đổi mật khẩu</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/doimatkhau.css">
    <link rel="stylesheet" href="${request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>
	
	<!-- Menu -->
   <%@ include file="/views/user/menu.jsp" %>
   <!-- End Menu -->

	<main>
        <div class="row pt-5 pb-5 ms-5 me-5">
            <div class="col-md-6 d-flex justify-content-center mt-5">
                <div class="card">
                    <div class="card-body">
                        <div class="text-center fs-2 fw-bold">Đổi Mật Khẩu</div>
                        <div class="mt-4">
                            <form action="${request.contextPath}/doi-mat-khau/submit" method="post">
                                <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Mật khẩu cũ" name="matKhauCu" value="${matKhauCu}">
                                        </div>
                                        <span class="text-danger">${messageMatKhauCu}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Mật khẩu mới" name="matKhauMoi" value="${matKhauMoi}">
                                        </div>
                                        <span class="text-danger">${messageMatKhauMoi}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Xác nhận mật khẩu mới" name="xacNhanMatKhauMoi" value="${xacNhanMatKhauMoi}">
                                        </div>
                                        <span class="text-danger">${messageXacNhanMatKhauMoi}</span>
                                        <span class="text-success">${messageSuccess}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-3 div-button">
                                    <div class="d-flex justify-content-end">
                                        <button class="btn btn-success rounded-3 fw-bold">Xác nhận</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 mt-2">
                <div class="d-flex justify-content-center">
                    <img src="${request.contextPath}/image/crush-chips.png" alt="">
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <%@ include file="/views/user/footer.jsp" %>
    <!-- End Footer -->
</body>
</html>