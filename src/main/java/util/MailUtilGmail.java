package util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailUtilGmail {

    // 1. Dán API Key lấy từ Resend vào đây (bắt đầu bằng re_...)
    private static final String RESEND_API_KEY = "re_cDnwoUB3_PA63BrycgsLyAfAjm5apYRaF";

    public static void sendMail(String to, String subject, String body, boolean bodyIsHTML) {
        try {
            URI uri = URI.create("https://api.resend.com/emails");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + RESEND_API_KEY.trim());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Escape chuỗi JSON an toàn
            String safeBody = body.replace("\"", "\\\"").replace("\n", "").replace("\r", "");
            String safeSubject = subject.replace("\"", "\\\"");

            // Resend cho phép gửi từ onboarding@resend.dev miễn phí tới email tài khoản của bạn
            String jsonPayload = "{"
                    + "\"from\": \"onboarding@resend.dev\","
                    + "\"to\": [\"" + to + "\"],"
                    + "\"subject\": \"" + safeSubject + "\","
                    + "\"html\": \"" + safeBody + "\""
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Resend API Response Code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}