package email;

import business.User;
import data.UserDB;
import util.MailUtilGmail;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
            } else {
                user.setActive(false);
                UserDB.insert(user);

                String domainUrl = "https://" + request.getServerName() + request.getContextPath();

                String encFirst = URLEncoder.encode(firstName, StandardCharsets.UTF_8);
                String encLast = URLEncoder.encode(lastName, StandardCharsets.UTF_8);
                String encEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

                String approveLink = domainUrl + "/emailList?action=admin_approve&email=" + encEmail
                        + "&firstName=" + encFirst + "&lastName=" + encLast;

                // Gửi email về cho BẠN (phuquy020105@gmail.com)
                String adminSubject = "[Phê Duyệt] Yêu cầu đăng ký từ: " + lastName + " " + firstName;
                String adminBody = "<div style='font-family: Arial, sans-serif; font-size: 15px; line-height: 1.6;'>"
                        + "<h3>Có người dùng đăng ký vào Email List:</h3>"
                        + "<p><b>Họ và tên:</b> " + lastName + " " + firstName + "</p>"
                        + "<p><b>Email:</b> " + email + "</p>"
                        + "<br>"
                        + "<p>Nhấn vào liên kết dưới đây để phê duyệt cho tài khoản này:</p>"
                        + "<p><a href='" + approveLink + "' style='background: #008080; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;'>XÁC NHẬN / PHÊ DUYỆT NGAY</a></p>"
                        + "</div>";

                MailUtilGmail.sendMail(MailUtilGmail.ADMIN_EMAIL, adminSubject, adminBody);

                // Chuyển sang trang màn hình xoay tròn chờ duyệt
                url = "/check_email.jsp";
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);

        } else if (action.equals("check_status")) {
            // Kiểm tra trạng thái kích hoạt ngầm
            String email = request.getParameter("email");
            User user = UserDB.getUser(email);
            boolean isApproved = (user != null && user.isActive());

            response.setContentType("application/json");
            response.getWriter().write("{\"approved\": " + isApproved + "}");
            return;

        } else if (action.equals("view_thanks")) {
            // Khi người dùng được duyệt, tự động chuyển về đây để nạp giao diện Thanks
            String email = request.getParameter("email");
            User user = UserDB.getUser(email);
            if (user != null) {
                request.setAttribute("user", user);
                url = "/thanks.jsp";
            } else {
                url = "/index.jsp";
            }

        } else if (action.equals("admin_approve")) {
            // Khi BẠN click vào liên kết duyệt trong hòm thư cá nhân
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");

            if (firstName == null) firstName = "";
            if (lastName == null) lastName = "";

            User user = UserDB.getUser(email);
            if (user == null) {
                user = new User(firstName, lastName, email);
            }
            user.setActive(true);
            UserDB.insert(user);

            // Màn hình trình duyệt khi bạn bấm phê duyệt xong
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("<div style='font-family: Arial, sans-serif; text-align: center; margin-top: 60px;'>"
                    + "<h2 style='color: #008080;'>Đã xác nhận thành công!</h2>"
                    + "<p>Màn hình của người đăng ký (" + email + ") đang được kích hoạt và chuyển sang trang hoàn tất.</p>"
                    + "</div>");
            return;
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