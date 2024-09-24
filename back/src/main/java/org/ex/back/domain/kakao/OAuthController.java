//package org.ex.back.domain.kakao;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@RestController
//public class OAuthController {
//
//    private final KakaoOAuthService kakaoOAuthService; // 액세스 토큰을 요청하는 서비스
//
//    public OAuthController(KakaoOAuthService kakaoOAuthService) {
//        this.kakaoOAuthService = kakaoOAuthService;
//    }
//
//    @GetMapping("/oauth")
//    public String oauthCallback(@RequestParam("code") String code) {
//        try {
//            // 액세스 토큰 요청
//            String accessToken = kakaoOAuthService.getAccessToken(code);
//
//            // 사용자 정보 요청
//            String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
//            HttpURLConnection conn = (HttpURLConnection) new URL(userInfoUrl).openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//
//            // 응답 확인
//            if (conn.getResponseCode() != 200) {
//                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                StringBuilder errorResponse = new StringBuilder();
//                String errorLine;
//                while ((errorLine = errorReader.readLine()) != null) {
//                    errorResponse.append(errorLine);
//                }
//                errorReader.close();
//                return "Error: " + errorResponse.toString(); // 오류 메시지 반환
//            }
//
//            // 응답 처리
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//            br.close();
//
//            // 사용자 정보 출력
//            return "Received code: " + code + ", Access Token: " + accessToken + ", User Info: " + response.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error occurred while processing the code.";
//        }
//    }
//}
