<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link rel="stylesheet"
	href="${request.ContextPath}/css/dangnhap.css">
</head>
<body>
	<div class="dangnhap">
        <div class="div-image">
            <img src="${request.contextPath}/image/backgrounddangnhap.png" alt="">
        </div>
        <div class="div-main">
            <div class="d-flex justify-content-center mt-5">
                <div class="card">
                    <div class="card-body">
                        <div class="text-center fs-2 fw-bold">Đăng nhập</div>
                        <div class="mt-4">
                            <f:form action="${request.contextPath}/dang-nhap/submit" method="post" modelAttribute="TaiKhoan">
                                <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-user"></i>
                                            <f:input type="text" placeholder="Tài khoản" path="tenDangNhap" value="${TaiKhoan.tenDangNhap}"/>
                                        </div>
                                        <f:errors class="text-danger" path="tenDangNhap"></f:errors>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center ${checkValid?'':'div-input'} mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <f:input type="password" placeholder="Mật khẩu" path="matKhau" value="${TaiKhoan.matKhau}"/>
                                        </div>
                                        <f:errors class="text-danger" path="matKhau"></f:errors>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-1">
                                    <div class="row div-cuoi"> 
                                        <div class="col-md-6">
                                            <input type="checkbox" name="remember" value="true" ${remember?'checked':''}> Lưu mật khẩu
                                        </div>
                                        <div class="col-md-6 d-flex justify-content-end">
                                            <a href="${request.contextPath}/quen-mat-khau">Quên mật khẩu</a>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-3 div-button">
                                    <div class="d-flex justify-content-center mt-3">
                                        <button class="btn btn-primary rounded-3 fw-bold">Đăng nhập</button>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-3 div-button">
                                    Bạn chưa có tài khoản? <a href="${request.contextPath}/dang-ky">Đăng ký</a> 
                                </div>
                            </f:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>