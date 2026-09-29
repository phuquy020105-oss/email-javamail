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
                UserDB.insert(user);

                String domainUrl = "https://" + request.getServerName() + request.getContextPath();

                // Mã hóa dữ liệu truyền trực tiếp trên URL để không bao giờ bị mất thông tin
                String encFirst = URLEncoder.encode(firstName, StandardCharsets.UTF_8);
                String encLast = URLEncoder.encode(lastName, StandardCharsets.UTF_8);
                String encEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

                String approveLink = domainUrl + "/emailList?action=admin_approve&email=" + encEmail
                        + "&firstName=" + encFirst + "&lastName=" + encLast;

                // Email gửi về hộp thư của BẠN (phuquy020105@gmail.com)
                String adminSubject = "[Phê Duyệt] Yêu cầu đăng ký từ: " + lastName + " " + firstName;
                String adminBody = "<div style='font-family: Arial, sans-serif; font-size: 15px; line-height: 1.6;'>"
                        + "<h3>Có yêu cầu đăng ký Email List mới:</h3>"
                        + "<p><b>Họ và tên:</b> " + lastName + " " + firstName + "</p>"
                        + "<p><b>Email:</b> " + email + "</p>"
                        + "<br>"
                        + "<p>Nhấn vào liên kết dưới đây để duyệt và kích hoạt tài khoản:</p>"
                        + "<p><a href='" + approveLink + "' style='background: #008080; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;'>XÁC NHẬN / PHÊ DUYỆT NGAY</a></p>"
                        + "</div>";

                MailUtilGmail.sendMail(MailUtilGmail.ADMIN_EMAIL, adminSubject, adminBody);

                url = "/check_email.jsp";
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);

        } else if (action.equals("admin_approve")) {
            // Khi BẠN click vào link trong Gmail:
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");

            if (firstName == null) firstName = "";
            if (lastName == null) lastName = "";

            User user = new User(firstName, lastName, email);
            user.setActive(true);
            UserDB.insert(user);

            // Gửi thông báo xác nhận thành công tới email admin để kiểm tra nội dung
            String userSubject = "Xác nhận đăng ký thông tin tài khoản thành công";
            String userBody = "<div style='font-family: Arial, sans-serif; font-size: 15px; line-height: 1.6;'>"
                    + "<p>Chào " + lastName + ",</p>"
                    + "<p>Hệ thống xác nhận bạn đã đăng ký thông tin thành công.<br>"
                    + "Thông tin tài khoản (" + email + ") đã được lưu trữ trên cơ sở dữ liệu.</p>"
                    + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua thư này.</p>"
                    + "<p>Trân trọng,<br>Bộ phận hỗ trợ kỹ thuật</p>"
                    + "</div>";

            MailUtilGmail.sendMail(MailUtilGmail.ADMIN_EMAIL, userSubject, userBody);

            // Chuyển thẳng sang trang Thanks for joining our email list
            request.setAttribute("user", user);
            url = "/thanks.jsp";
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