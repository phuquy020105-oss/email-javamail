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

                // Đường dẫn web để tạo link xác nhận trong email
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

                String subject = "Please confirm your email subscription";
                String body = "<h3>Hello " + firstName + " " + lastName + ",</h3>"
                        + "<p>Thanks for subscribing. Please click the link below to confirm your registration:</p>"
                        + "<p><a href='" + confirmLink + "'>" + confirmLink + "</a></p>"
                        + "<br><p>Best regards,<br>Email List Team</p>";

                try {
                    MailUtilGmail.sendMail(email, subject, body, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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