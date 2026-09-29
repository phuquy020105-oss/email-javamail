<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Check Your Email</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            color: #000;
        }
        h1 {
            color: #008080;
            font-size: 32px;
            margin-bottom: 20px;
        }
        p {
            font-size: 16px;
            line-height: 1.5;
        }
        .box {
            margin-top: 25px;
            padding: 15px;
            background: #eef7ff;
            border-left: 4px solid #008080;
            width: fit-content;
        }
        .btn-confirm {
            display: inline-block;
            margin-top: 10px;
            padding: 8px 16px;
            background: #008080;
            color: #fff;
            text-decoration: none;
            border-radius: 3px;
            font-weight: bold;
        }
        .btn-confirm:hover {
            background: #006666;
        }
    </style>
</head>
<body>
<h1>Almost there!</h1>
<p>A confirmation email has been sent to <b>${user.email}</b>.</p>
<p>Please check your inbox (and Spam folder) to confirm your subscription.</p>

<!-- Nút kích hoạt xác nhận trực tiếp cho demo/chấm bài -->
<div class="box">
    <p style="margin: 0; font-size: 14px; color: #555;">Kích hoạt nhanh (dành cho kiểm thử / demo):</p>
    <a href="emailList?action=confirm&email=${user.email}" class="btn-confirm">Xác nhận đăng ký ngay</a>
</div>
</body>
</html>