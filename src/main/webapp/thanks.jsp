<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanks for joining</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; color: #000; }
        h1 { color: #008080; font-size: 32px; margin-bottom: 20px; }
        p { font-size: 16px; margin-bottom: 20px; }
        .info-row { margin-bottom: 10px; font-size: 16px; }
        .info-label { display: inline-block; width: 110px; font-weight: bold; }
        .btn-return { padding: 4px 14px; font-size: 14px; cursor: pointer; border: 1px solid #7a7a7a; border-radius: 3px; background: #e9e9e9; }
    </style>
</head>
<body>
<h1>Thanks for joining our email list</h1>
<p>Here is the information that you entered:</p>

<div class="info-row">
    <span class="info-label">Email:</span>
    <span>${user.email}</span>
</div>
<div class="info-row">
    <span class="info-label">First Name:</span>
    <span>${user.firstName}</span>
</div>
<div class="info-row">
    <span class="info-label">Last Name:</span>
    <span>${user.lastName}</span>
</div>

<p style="margin-top: 30px;">To enter another email address, click on the Back button in your browser or the Return button shown below.</p>

<form action="emailList" method="post">
    <input type="hidden" name="action" value="join">
    <input type="submit" value="Return" class="btn-return">
</form>
</body>
</html>