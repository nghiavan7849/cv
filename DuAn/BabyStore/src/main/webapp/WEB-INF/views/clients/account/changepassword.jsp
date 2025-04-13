<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đổi mật khẩu</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
	integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>

<link rel="stylesheet" href="./css/QuenMK.css">
</head>
<body style="font-family: 'Arial', sans-serif; background-color: #f5f5f5;">
    <br>
    <br>
    <br>
    <div class="container-fluid">
        <article style="background-image: linear-gradient(#81c408, #f5f5f5); height: 729px;">
            <div class="container d-flex justify-content-center" style="padding-top: 160px;">
                <div class="card shadow p-4" style="width: 500px; border-radius: 10px; background-color: #fff;">
                    <h2 style="font-weight: bold; font-size: 24px; color: #2c3e50; text-transform: uppercase; letter-spacing: 1px; text-align: center;">ĐỔI MẬT KHẨU</h2>
                    <form action="/forget/changepassword" method="post" onsubmit="return validateForm()" style="padding: 20px;">
                        <div class="mb-3">
                            <label for="newPass" class="form-label" style="color: #495057;">Mật Khẩu Mới</label>
                            <input type="password" class="form-control" id="newPass" placeholder="Nhập Mật Khẩu Mới" name="newPass" required style="border-radius: 8px; padding: 12px; border: 1px solid #ced4da;">
                        </div>
                        <div class="mb-3">
                            <label for="confirmPass" class="form-label" style="color: #495057;">Xác Nhận Mật Khẩu</label>
                            <input type="password" class="form-control" id="confirmPass" placeholder="Xác Nhận Mật Khẩu" name="confirmPass" required style="border-radius: 8px; padding: 12px; border: 1px solid #ced4da;">
                        </div>
                        <input type="hidden" class="form-control" id="id" name="id" value="${account.id}">
                        <label id="message" class="text-danger d-block text-center mb-3">${message}</label>
                        <button type="submit" class="btn btn-success btn-block" style="border-radius: 8px; padding: 12px; font-size: 16px; background-color: #28a745; color: #fff; border: 1px solid #28a745; cursor: pointer; transition: background-color 0.3s ease, transform 0.2s ease;"
                            onmouseover="this.style.backgroundColor='#218838'; this.style.borderColor='#218838'; this.style.transform='translateY(-2px)'" onmouseout="this.style.backgroundColor='#28a745'; this.style.borderColor='#28a745'; this.style.transform='translateY(0)'">
                            Xác nhận
                        </button>
                    </form>
                </div>
            </div>
        </article>
    </div>

    <script>
        function validateForm() {
            var newPassword = document.getElementById('newPass').value;
            var confirmPassword = document.getElementById('confirmPass').value;
            var messageElement = document.getElementById('message');

            if (newPassword.length < 8) {
                messageElement.textContent = 'Mật khẩu mới phải có ít nhất 8 ký tự.';
                return false;
            }

            if (newPassword !== confirmPassword) {
                messageElement.textContent = 'Mật khẩu mới và xác nhận không khớp.';
                return false;
            }

            messageElement.textContent = ''; // Clear previous error message
            return true;
        }
    </script>
</body>
</html>