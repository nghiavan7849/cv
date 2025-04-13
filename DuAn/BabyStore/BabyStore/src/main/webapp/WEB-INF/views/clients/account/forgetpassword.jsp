<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- <!DOCTYPE html> -->
<html>
<head>
<meta charset="UTF-8">
<title>Đổi mật khẩu</title>
</head>
<body style="font-family: 'Arial', sans-serif; background-color: #f5f5f5;">
    <br>
    <br>
    <br>
    <div class="container-fluid">
        <article style="background-image: linear-gradient(#81c408, #f5f5f5); height: 729px;">
            <div class="container d-flex justify-content-center" style="padding: 160px;">
                <div class="form-container shadow" style="margin-top: 160px; width: 600px; background: linear-gradient(to bottom right, #fff, #f1f1f1); border-radius: 15px; padding: 30px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); transition: transform 0.3s ease;">
                    <div class="form-control border-0" style="width: 100%;">
                        <form action="/forget/checkMail" method="post">
                            <div class="form-title mb-3" style="text-align: center;">
                                <h2 style="font-weight: bold; font-size: 24px; color: #2c3e50; text-transform: uppercase; letter-spacing: 1px;">QUÊN MẬT KHẨU</h2>
                            </div>
                            <label class="text-danger" style="display: block; text-align: center; margin-bottom: 10px;">${message}</label>
                            <div class="form-floating mb-3" style="position: relative; margin-bottom: 1.5rem;">
                                <input type="email" class="form-control" id="floatingInput" placeholder="Nhập Email Tài Khoản" name="email" style="border-radius: 10px; padding: 20px; font-size: 16px; box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1); border: 2px solid #81c408;">
                                <label for="floatingInput" style="position: absolute; top: 50%; left: 15px; transform: translateY(-50%);padding: 0 5px; color: #666; transition: all 0.3s ease; pointer-events: none;">Email address</label>
                            </div>
                            <button type="submit" class="btn btn-dark mb-4" style="width: 100%; border-radius: 10px; background-color: #333; color: white; padding: 15px; font-size: 18px; transition: background-color 0.3s ease, transform 0.3s ease;">
                                LẤY LẠI MẬT KHẨU
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </article>
    </div>

    <script>
        document.querySelector('.form-container').addEventListener('mouseover', function() {
            this.style.transform = 'scale(1.02)';
            this.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.2)';
        });

        document.querySelector('.form-container').addEventListener('mouseout', function() {
            this.style.transform = 'scale(1)';
            this.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
        });

        document.querySelector('.btn-dark').addEventListener('mouseover', function() {
            this.style.backgroundColor = '#555';
            this.style.transform = 'translateY(-2px)';
        });

        document.querySelector('.btn-dark').addEventListener('mouseout', function() {
            this.style.backgroundColor = '#333';
            this.style.transform = 'translateY(0)';
        });

        document.querySelector('#floatingInput').addEventListener('focus', function() {
            this.style.outline = 'none';
            this.style.boxShadow = '0 0 8px rgba(129, 196, 8, 0.5)';
        });

        document.querySelector('#floatingInput').addEventListener('blur', function() {
            this.style.boxShadow = 'inset 0 2px 4px rgba(0, 0, 0, 0.1)';
        });

        document.querySelector('#floatingInput').addEventListener('input', function() {
            if (this.value !== '') {
                this.nextElementSibling.style.top = '0';
                this.nextElementSibling.style.fontSize = '12px';
                this.nextElementSibling.style.color = '#81c408';
            } else {
                this.nextElementSibling.style.top = '50%';
                this.nextElementSibling.style.fontSize = '16px';
                this.nextElementSibling.style.color = '#666';
            }
        });
    </script>
</body>



</html>
