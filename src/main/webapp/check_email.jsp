<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đang chờ phê duyệt</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 75vh;
            margin: 0;
            color: #333;
        }
        .spinner {
            width: 55px;
            height: 55px;
            border: 6px solid #f3f3f3;
            border-top: 6px solid #008080;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin-bottom: 25px;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        h2 {
            color: #008080;
            margin-bottom: 12px;
            font-size: 24px;
        }
        p {
            font-size: 16px;
            color: #666;
            margin: 0;
        }
    </style>
</head>
<body>
<div class="spinner"></div>
<h2>Đang chờ xác nhận từ quản trị viên...</h2>
<p>Vui lòng giữ nguyên màn hình, hệ thống sẽ tự động chuyển trang ngay khi được phê duyệt.</p>

<script>
    const email = "${user.email}";

    // Cứ mỗi 1.5 giây gửi yêu cầu kiểm tra xem bạn đã bấm duyệt trong mail chưa
    const timer = setInterval(() => {
        fetch("emailList?action=check_status&email=" + encodeURIComponent(email))
            .then(res => res.json())
            .then(data => {
                if (data.approved) {
                    clearInterval(timer);
                    // Tự động chuyển thẳng sang màn hình Thanks
                    window.location.href = "emailList?action=view_thanks&email=" + encodeURIComponent(email);
                }
            })
            .catch(err => console.log(err));
    }, 1500);
</script>
</body>
</html>