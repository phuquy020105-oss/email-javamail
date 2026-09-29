<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Email List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; color: #000; }
        h1 { color: #008080; font-size: 32px; margin-bottom: 20px; }
        p { font-size: 16px; margin-bottom: 15px; }
        .error-message { color: red; font-style: italic; margin-bottom: 15px; line-height: 1.4; }
        .form-row { margin-bottom: 12px; display: flex; align-items: center; }
        .form-row label { width: 110px; font-size: 16px; font-weight: bold; }
        .form-row input[type="text"], .form-row input[type="email"] {
            width: 250px; padding: 4px 8px; font-size: 15px; border: 1px solid #7a7a7a; border-radius: 3px;
        }
        .btn-submit { margin-top: 8px; padding: 4px 14px; font-size: 14px; cursor: pointer; border: 1px solid #7a7a7a; border-radius: 3px; background: #e9e9e9; }
    </style>
</head>
<body>
<h1>Join our email list</h1>
<p>To join our email list, enter your name and email address below.</p>

<% if (request.getAttribute("message") != null && !((String)request.getAttribute("message")).isEmpty()) { %>
<div class="error-message">
    <%= request.getAttribute("message") %>
</div>
<% } %>

<form action="emailList" method="post">
    <input type="hidden" name="action" value="add">

    <div class="form-row">
        <label>Email:</label>
        <input type="email" name="email" value="${user.email}" required>
    </div>
    <div class="form-row">
        <label>First Name:</label>
        <input type="text" name="firstName" value="${user.firstName}" required>
    </div>
    <div class="form-row">
        <label>Last Name:</label>
        <input type="text" name="lastName" value="${user.lastName}" required>
    </div>

    <input type="submit" value="Join Now" class="btn-submit">
</form>
</body>
</html>