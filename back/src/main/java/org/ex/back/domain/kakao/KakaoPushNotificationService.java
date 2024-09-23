package org.ex.back.domain.kakao;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class KakaoPushNotificationService {

    private final String pushUrl = "https://kapi.kakao.com/v2/push/send";

    public void sendPushNotification(String accessToken, String orderNumber) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(pushUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");

        // JSON으로 메시지 작성
        String jsonBody = String.format("{\"template_id\": \"YOUR_TEMPLATE_ID\", \"template_args\": {\"order_number\": \"%s\"}}", orderNumber);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes());
            os.flush();
        }

        // 응답 처리
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + ", " + errorResponse.toString());
        }
    }
}
