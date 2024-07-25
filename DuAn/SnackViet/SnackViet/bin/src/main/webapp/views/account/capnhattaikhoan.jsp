<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cập nhật tài khoản</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/capnhattaikhoan.css">
<link rel="stylesheet" href="${request.contextPath}/css/menu.css">
<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>
	<!-- Menu -->
   <%@ include file="/views/user/menu.jsp" %>
   <!-- End Menu -->
	<main>
        <div class="row pt-5 ms-5 me-5">
            <div class="col-md-6 d-flex justify-content-center">
                <div class="card">
                    <div class="card-body">
                        <div class="div-title">
                        	<div class="text-center fs-2 fw-bold">Cập nhật tài khoản</div>
                        	<span class="text-success">${messageSuccess}</span>
                        </div>
                        <div class="">
                            <form action="/cap-nhat-tai-khoan/submit" method="post" enctype="multipart/form-data">
                                <div class="d-flex justify-content-center div-input">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-user"></i>
                                            <input type="text" placeholder="Họ và tên" name="hoVaTen" value="${taiKhoan.hoVaTen}">
                                        </div>
                                        <span class="text-danger">${messageHoVaTen}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-phone"></i>
                                            <input type="text" placeholder="Số điện thoại" name="soDienThoai" value="${taiKhoan.soDienThoai}">
                                        </div>
                                        <span class="text-danger">${messageSoDienThoai}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-regular fa-envelope"></i>
                                            <input type="text" placeholder="Email" name="email" value="${taiKhoan.email}">
                                        </div>
                                        <span class="text-danger">${messageEmail}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-1">
                                    <div class="d-flex justify-content-start" style="width:350px; height: 51px;">
                                        <div>
                                            <label class="fs-5" for="">Giới tính:</label>
                                            <input type="radio" class="ms-3" name="gioiTinh" id="" value="true" ${taiKhoan.gioiTinh?'checked':''} > Nam
                                            <input type="radio" class="ms-2" name="gioiTinh" id="" value="false" ${taiKhoan.gioiTinh?'':'checked'}> Nữ <br>
                                            <span class="text-danger"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-1">
                                    <div class="d-flex justify-content-start" style="width:350px;">
                                        <div class="row">
                                            <label class="fs-5 p-0 " for="">Hình ảnh:</label>
                                            <input type="file" class="form-control col-md-9" id="" name="file"> 
                                            <span class="text-danger p-0"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-4 mb-2 div-button">
                                    <div class="d-flex justify-content-end">
                                        <button class="btn btn-success rounded-3 fw-bold">Lưu thay đổi</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
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