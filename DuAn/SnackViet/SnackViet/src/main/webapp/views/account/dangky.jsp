<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng ký</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/dangky.css">
</head>
<body>
	<div class="dangnhap">
        <div class="div-image">
            <img src="${request.contextPath}/image/backgrounddangnhap.png" alt="">
        </div>
        <div class="div-main">
            <div class="d-flex justify-content-center mt-2">
                <div class="card">
                    <div class="card-body">
                        <div class="text-center fs-2 fw-bold" >Đăng ký</div>
                       <span class="text-warning">${messageSuccess}</span>
                        <div class="mt-4">
                             <form action="${request.contextPath}/dang-ky/submit" method="post">
                               <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-user"></i>
                                            <input type="text" placeholder="Họ và tên" name="hoVaTen"  value="${hoVaTen}">
                                        </div>
                                        <span class="text-danger">${messageHoVaTen}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-user"></i>
                                            <input type="text" placeholder="Tên đăng nhập" name="tenDangNhap"  value="${tenDangNhap}">
                                        </div>
                                        <span class="text-danger">${messageTenDangNhap}</span>
                                    </div>
                                </div>
                                 <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-phone"></i>
                                            <input type="text" placeholder="Số điện thoại" name="soDienThoai"  value="${soDienThoai}">
                                        </div>
                                        <span class="text-danger">${messageSoDienThoai}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-regular fa-envelope"></i>
                                            <input type="text" placeholder="Email" name="email" value="${email}">
                                        </div>
                                        <span class="text-danger">${messageEmail}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Mật khẩu " name="matKhau"  value="${matKhau}">
                                        </div>
                                        <span class="text-danger">${messageMatKhau}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Xác Nhận Mật khẩu " name="xnmatKhau"  value="${xnmatKhau}" >
                                        </div>
                                        <span class="text-danger">${messageXacNhanMatKhau}</span>
                                    </div>
                                </div>
                               
                                <div class="d-flex justify-content-center div-button">
                                    <div class="d-flex justify-content-center mt-3">
                                        <button class="btn btn-primary rounded-3 fw-bold">Đăng ký</button>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-3 div-button">
                                    Bạn đã có tài khoản?  <a href="${request.contextPath}/dang-nhap"> Đăng nhập</a> 
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>