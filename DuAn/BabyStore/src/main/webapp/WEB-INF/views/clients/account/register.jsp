<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            width: 400px;
            background-color: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: bold;
            margin-bottom: 8px;
            color: #555;
        }

        input[type=text], input[type=password], input[type=email] {
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        input[type=submit] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 12px 20px;
            text-align: center;
            text-decoration: none;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        input[type=submit]:hover {
            background-color: #45a049;
        }

        .error-message {
            background-color: #f2dede;
            border: 1px solid #ebccd1;
            color: #a94442;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            font-size: 14px;
        }

        .success-message {
            background-color: #dff0d8;
            border: 1px solid #d0e9c6;
            color: #3c763d;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            font-size: 14px;
        }

        .register-link {
            margin-top: 20px;
            text-align: center;
        }

        .register-link a {
            color: #4CAF50;
            text-decoration: none;
        }

        .register-link a:hover {
            text-decoration: underline;
        }

        .register-link p {
            margin: 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Đăng ký tài khoản</h2>

        <%-- Display errors near respective form fields --%>
        <form action="register" method="post">
            <label for="userName">Tên đăng nhập:</label>
            <input type="text" id="userName" name="userName" value="${account.userName}" required>
            <% if (request.getAttribute("userNameError") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("userNameError") %>
                </div>
            <% } %>

            <label for="password">Mật khẩu:</label>
            <input type="password" id="password" name="password" value="${account.password}" required>
            <% if (request.getAttribute("passwordError") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("passwordError") %>
                </div>
            <% } %>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${account.email}" required>
            <% if (request.getAttribute("emailError") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("emailError") %>
                </div>
            <% } %>

            <label for="fullName">Họ và tên:</label>
            <input type="text" id="fullName" name="fullName" value="${account.fullName}" required>
            <% if (request.getAttribute("fullNameError") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("fullNameError") %>
                </div>
            <% } %>

            <input type="submit" value="Đăng ký">

            <div class="register-link">
                <p>Chuyển đến trang đăng nhập <a href="/login">Đăng nhập ngay</a></p>
            </div>
        </form>

        <%-- Display success message if registration is successful --%>
        <% if (request.getAttribute("successMessage") != null) { %>
            <div class="success-message">
                <%= request.getAttribute("successMessage") %>
            </div>
        <% } %>
    </div>
</body>
</html>
