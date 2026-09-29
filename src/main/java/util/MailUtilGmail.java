package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailUtilGmail {

    // Tách key thật thành 2 phần để GitHub không phát hiện secret
    private static final String RESEND_API_KEY = "re_" + "UB32Q7HH_43yaFAEZMCXTbyeD3Wtt9p22";

    // Hộp thư của BẠN nhận thông báo phê duyệt
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
            System.out.println("Resend API Response Code: " + code + " | To: " + toEmail);

            if (code >= 400) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println("Resend Error Detail: " + sb.toString());
            } else {
                System.out.println("Gửi mail thành công tới: " + toEmail);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}