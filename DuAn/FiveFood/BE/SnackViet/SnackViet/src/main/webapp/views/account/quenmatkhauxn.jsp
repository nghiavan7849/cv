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
<link rel="stylesheet" href="${request.contextPath}/css/quenmatkhauxn.css">
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
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Mật khẩu mới"  name="newPassword" value="${valueNewPass}">
                                        </div>
                                        <span class="text-danger">${messageNewPass}</span>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center div-input mt-1">
                                    <div class="input-box">
                                        <div>
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="password" placeholder="Mã xác nhận mật khẩu mới" name="confirmNewPassword" value="${valueConfirmNewPass}">
                                        </div>
      									<span class="text-danger">${messageConfirmNewPass}</span>
                                        <span class="text-success">${messageSuccess}</span>                                      
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-1 div-button">
                                    <div class="d-flex justify-content-center mt-3">
                                        <button class="btn btn-primary rounded-3 fw-bold" formaction="${request.contextPath}/quen-mat-khau-xac-nhan" formmethod="post">Xác nhận</button>
                                    </div>
                                    <div class="d-flex justify-content-center mt-3">
                                        <button class="btn btn-secondary rounded-3 fw-bold" formaction="${request.contextPath}/quen-mat-khau-xac-nhan/back" formmethod="get">Trở lại</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
     <script type="text/javascript">
    	document.addEventListener("DOMContentLoaded", function() {
   			var message = '${messageSuccess}';
   			var protocol = window.location.protocol;
    		if(message != null && message == 'Đổi mật khẩu thành công'){
    			alert(message);
    			url = protocol + "/dang-nhap";
    			window.location.href = url;
    			
    		}
    	});
    
    </script>
</body>
</html>