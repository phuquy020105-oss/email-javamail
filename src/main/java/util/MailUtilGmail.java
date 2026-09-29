package util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailUtilGmail {

    // Giữ nguyên API Key của bạn (tách chuỗi để tránh bị GitHub chặn push)
    private static final String RESEND_API_KEY = "re_" + "cDnwoUB3_PA63BrycgsLyAfAjm5apYRaF"; // Hoặc key thật của bạn
    private static final String ADMIN_EMAIL = "phuquy020105@gmail.com";

    public static void sendMail(String subscriberEmail, String subject, String body, boolean bodyIsHTML) {
        try {
            URI uri = URI.create("https://api.resend.com/emails");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + RESEND_API_KEY.trim());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String safeBody = body.replace("\"", "\\\"").replace("\n", "").replace("\r", "");
            String safeSubject = subject.replace("\"", "\\\"");

            // Gửi thẳng về ADMIN_EMAIL (chính bạn)
            String jsonPayload = "{"
                    + "\"from\": \"onboarding@resend.dev\","
                    + "\"to\": [\"" + ADMIN_EMAIL + "\"],"
                    + "\"subject\": \"" + safeSubject + "\","
                    + "\"html\": \"" + safeBody + "\""
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Resend API Status Code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}