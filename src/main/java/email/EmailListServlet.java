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

                // Tạo đường dẫn xác thực tuyệt đối
                String scheme = request.getScheme();
                String serverName = request.getServerName();
                int serverPort = request.getServerPort();
                String contextPath = request.getContextPath();

                String domainUrl = scheme + "://" + serverName;
                if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
                    domainUrl += ":" + serverPort;
                }
                domainUrl += contextPath;

                String confirmLink = domainUrl + "/emailList?action=confirm&email=" + email;

                // Nội dung email gửi về hộp thư của BẠN
                String subject = "[Admin Xác Nhận] Có người đăng ký email mới: " + email;
                String body = "<h3>Yêu cầu đăng ký Email List mới</h3>"
                        + "<p><b>Họ và tên:</b> " + firstName + " " + lastName + "</p>"
                        + "<p><b>Email đăng ký:</b> " + email + "</p>"
                        + "<p>Bấm vào liên kết dưới đây để phê duyệt / xác thực:</p>"
                        + "<p><a href='" + confirmLink + "'>Xác nhận đăng ký (" + confirmLink + ")</a></p>";

                // Gửi mail về hộp thư admin của bạn
                MailUtilGmail.sendMail(email, subject, body, true);

                url = "/check_email.jsp";
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);

        } else if (action.equals("confirm")) {
            String email = request.getParameter("email");
            User user = UserDB.getUser(email);

            if (user != null) {
                user.setActive(true);
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