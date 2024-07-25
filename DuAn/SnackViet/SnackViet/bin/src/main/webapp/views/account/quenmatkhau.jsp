<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quên mật khẩu</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/quenmatkhau.css">
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
                        <div class="text-center fs-2 fw-bold">Quên mật khẩu</div>
                        <div class="mt-4">
                            <form>
                                <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-envelope"></i>
                                            <input type="text" placeholder="Email" name="email" value="${valueEmail}">
                                        </div>
	`									<span class="text-danger">${messageEmail}</span>
                                        <span class="text-success">${messageSuccess}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center" >
                                    <div class="d-flex justify-content-between div-input" style="width: 320px;">
                                        <div class="input-box">
                                            <div class="input-dau"> 
                                                <i class="fa-solid fa-key"></i>
                                                <input type="text" placeholder="Mã xác nhận" name="OTP" value="${valueOTP}"><br>
                                                <span class="text-danger">${messageCode}</span>
                                            </div>
                                        </div>
                                        <div class="input-box">
                                            <div class="input-dau"> 
                                                <button class="btn btn-primary fw-bold" formaction="${request.contextPath}/quen-mat-khau/send-otp" formmethod="post"${checkSendOtp?'disabled':''} >Gửi</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="d-flex justify-content-center mt-1 div-button">
                                    <div class="d-flex justify-content-center mt-3">
                                        <button class="btn btn-primary rounded-3 fw-bold" ${checkSendOtp?'':'disabled'}  formaction="${request.contextPath}/quen-mat-khau/next" formmethod="post">Tiếp tục</button>
                                    </div>
                                    <div class="d-flex justify-content-center mt-3">
                                        <button class="btn btn-secondary rounded-3 fw-bold" formaction="${request.contextPath}/quen-mat-khau/back" formmethod="post">Trở lại</button>
                                    </div>
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