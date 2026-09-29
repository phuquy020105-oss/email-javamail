package email;

import business.User;
import data.UserDB;
import util.MailUtilGmail;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }

        String url = "/index.jsp";

        if (action.equals("join")) {
            url = "/index.jsp";

        } else if (action.equals("add")) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            User user = new User(firstName, lastName, email);
            String message = "";

            if (firstName == null || lastName == null || email == null ||
                    firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty()) {
                message = "Please fill out all three text boxes.";
                url = "/index.jsp";
            } else if (UserDB.emailExists(email)) {
                message = "This email address already exists.<br>Please enter another email address.";
                url = "/index.jsp";
            } else {
                UserDB.insert(user);

                // Lấy URL hiện tại của Render
                String scheme = request.getScheme();
                String serverName = request.getServerName();
                int serverPort = request.getServerPort();
                String contextPath = request.getContextPath();

                String domainUrl = scheme + "://" + serverName;
                if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
                    domainUrl += ":" + serverPort;
                }
                domainUrl += contextPath;

                // Link xác nhận dành riêng cho BẠN (Admin) bấm phê duyệt
                String approveLink = domainUrl + "/emailList?action=admin_approve&email=" + email;

                // Gửi thư thông báo về hòm thư của BẠN
                String adminSubject = "[Yêu Cầu Phê Duyệt] Người dùng mới đăng ký: " + firstName + " " + lastName;
                String adminBody = "<div style='font-family: Arial, sans-serif; font-size: 15px;'>"
                        + "<h3>Có một người dùng vừa gửi thông tin đăng ký:</h3>"
                        + "<p><b>Họ và tên:</b> " + lastName + " " + firstName + "</p>"
                        + "<p><b>Email:</b> " + email + "</p>"
                        + "<br>"
                        + "<p>Vui lòng bấm vào nút bên dưới để xác nhận phê duyệt:</p>"
                        + "<p><a href='" + approveLink + "' style='background:#008080; color:#fff; padding:10px 18px; text-decoration:none; border-radius:4px; font-weight:bold; display:inline-block;'>Phê duyệt & Gửi mail xác nhận</a></p>"
                        + "</div>";

                MailUtilGmail.sendMail(MailUtilGmail.ADMIN_EMAIL, adminSubject, adminBody);

                // Trên màn hình web của người đăng ký: Chuyển thẳng sang trang Thanks for joining (Ảnh 1)
                url = "/thanks.jsp";
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);

        } else if (action.equals("admin_approve")) {
            // Khi BẠN mở mail và bấm nút Phê duyệt:
            String email = request.getParameter("email");
            User user = UserDB.getUser(email);

            if (user != null) {
                user.setActive(true);

                // Gửi thư kích hoạt tài khoản thành công đến email của người đăng ký (Nội dung đúng như Ảnh 2 của bạn)
                String userSubject = "Xác nhận đăng ký thông tin tài khoản";
                String userBody = "<div style='font-family: Arial, sans-serif; font-size: 15px; line-height: 1.6;'>"
                        + "<p>Chào " + user.getLastName() + ",</p>"
                        + "<p>Hệ thống xác nhận bạn đã đăng ký thông tin thành công.<br>"
                        + "Thông tin tài khoản của bạn đã được lưu trữ trên cơ sở dữ liệu.</p>"
                        + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua thư này.</p>"
                        + "<p>Trân trọng,<br>Bộ phận hỗ trợ kỹ thuật</p>"
                        + "</div>";

                // Gửi mail cho khách
                MailUtilGmail.sendMail(email, userSubject, userBody);

                request.setAttribute("user", user);
                url = "/thanks.jsp";
            } else {
                url = "/index.jsp";
            }
        }

        ServletContext sc = getServletContext();
        sc.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}