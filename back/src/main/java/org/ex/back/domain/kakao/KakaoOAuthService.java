//package org.ex.back.domain.kakao;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@Service
//public class KakaoOAuthService {
//
//    @Value("${kakao.rest.api.key}")
//    private String apiKey;
//
//    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";
//
//    //엑세스 토큰 요청
//    public String getAccessToken(String code) throws Exception {
//        URL url = new URL(tokenUrl);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//        String body = "grant_type=authorization_code&client_id=" + apiKey + "&redirect_uri=http://localhost:8080/oauth&code=" + code;
//
//        try (OutputStream os = conn.getOutputStream()) {
//            os.write(body.getBytes());
//            os.flush();
//        }
//
//        // 응답 코드 확인
//        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            StringBuilder errorResponse = new StringBuilder();
//            String errorLine;
//            while ((errorLine = errorReader.readLine()) != null) {
//                errorResponse.append(errorLine);
//            }
//            errorReader.close();
//            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + ", " + errorResponse.toString());
//        }
//
//        // 응답 읽기
//        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        StringBuilder response = new StringBuilder();
//        String line;
//        while ((line = br.readLine()) != null) {
//            response.append(line);
//        }
//        br.close();
//
//        // JSON 파싱 로직 추가
//        return extractAccessToken(response.toString());
//    }
//
//    //엑세스 토큰 추출
//    private String extractAccessToken(String responseBody) {
//        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject(); // JSON 파싱
//        return jsonObject.get("access_token").getAsString(); // 실제 액세스 토큰 반환
//    }
//}
