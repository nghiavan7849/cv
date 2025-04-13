<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
</head>
<body style="font-family: 'Arial', sans-serif; background-color: #f5f5f5;">
    <div class="container-fluid">
        <article style="background-image: linear-gradient(#81c408, #f5f5f5); height: 729px;">
            <div class="container d-flex justify-content-center" style="padding-top: 160px;">
                <div class="form-container shadow p-4" style="width: 500px; margin-top:150px; background-color: #fff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                    <form action="/forget/OTP" method="post" onsubmit="return validateForm()" style="padding: 20px;">
                        <h2 style="font-weight: bold; color: #333; text-align: center;">NHẬP MÃ OTP</h2>
                        <label class="text-primary d-block text-center mb-3" id="message">${message}</label>
                        <div class="form-floating mb-3">
                            <input type="number" class="form-control" id="floatingInput" placeholder="Nhập OTP" name="otp" required style="border-radius: 8px; padding: 12px; border: 1px solid #ced4da;">
                            <label for="floatingInput" style="font-size: 14px; color: #666;">Nhập mã OTP</label>
                        </div>
                        <button type="submit" class="btn btn-success btn-block" style="border-radius: 8px; padding: 12px; font-size: 16px; background-color: #28a745; color: #fff; border: 1px solid #28a745; cursor: pointer; transition: background-color 0.3s ease, transform 0.2s ease;" onmouseover="this.style.backgroundColor='#218838'; this.style.borderColor='#218838'; this.style.transform='translateY(-2px)'" onmouseout="this.style.backgroundColor='#28a745'; this.style.borderColor='#28a745'; this.style.transform='translateY(0)'">
                            Xác nhận
                        </button>
                    </form>
                </div>
            </div>
        </article>
    </div>

    <script>
        function validateForm() {
            var otpInput = document.getElementById('floatingInput').value;
            var messageElement = document.getElementById('message');
            
            if (otpInput.length !== 6) {
                messageElement.textContent = 'Mã OTP phải có đúng 6 chữ số.';
                messageElement.style.color = 'red';
                return false;
            } else {
                messageElement.textContent = ''; // Clear previous error message
                return true;
            }
        }
    </script>
</body>
</html>