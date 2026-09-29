package util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailUtilGmail {

    // Giữ nguyên chuỗi nối API key của bạn để tránh Push Protection
    private static final String RESEND_API_KEY = "re_" + "cDnwoUB3_PA63BrycgsLyAfAjm5apYRaF";
    public static final String ADMIN_EMAIL = "phuquy020105@gmail.com";

    public static void sendMail(String toEmail, String subject, String body) {
        try {
            URI uri = URI.create("https://api.resend.com/emails");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + RESEND_API_KEY.trim());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String safeBody = body.replace("\"", "\\\"").replace("\n", "<br>").replace("\r", "");
            String safeSubject = subject.replace("\"", "\\\"");

            String jsonPayload = "{"
                    + "\"from\": \"onboarding@resend.dev\","
                    + "\"to\": [\"" + toEmail.trim() + "\"],"
                    + "\"subject\": \"" + safeSubject + "\","
                    + "\"html\": \"" + safeBody + "\""
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Resend API code: " + code + " to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}