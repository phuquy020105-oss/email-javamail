package util;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtilGmail {

    // Điền tài khoản Gmail gửi và 16 ký tự lấy từ Google ở Bước 1
    private static final String SENDER_EMAIL = "lephuquy020105@gmail.com";
    private static final String SENDER_APP_PASSWORD = "dnjm xrdd gygi fnrq"; // Thay 16 ký tự vào đây

    public static void sendMail(String to, String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", "465");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_APP_PASSWORD.replace(" ", ""));
            }
        });

        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        Address fromAddress = new InternetAddress(SENDER_EMAIL);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}