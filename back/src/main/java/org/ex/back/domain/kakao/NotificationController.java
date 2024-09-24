//package org.ex.back.domain.firebase;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//
//@RestController
//@RequestMapping("/api/notifications")
//public class NotificationController {
//
//    @Autowired
//    private KakaoPushNotificationService kakaoPushNotificationService;
//
//    @Autowired
//    private KakaoOAuthService kakaoOAuthService;
//
//    @PostMapping("/send")
//    public void sendNotification(@RequestBody NotificationRequest request) {
//        try {
//            // 액세스 토큰을 발급받는 로직
//            String accessToken = kakaoOAuthService.getAccessToken(request.getCode()); // request에서 code를 받아오는 방법 필요
//            kakaoPushNotificationService.sendPushNotification(accessToken, request.getDeviceToken(), request.getTitle(), request.getBody());
//        } catch (Exception e) {
//            // 예외 처리 로직
//            e.printStackTrace(); // 로그에 출력
//            // 적절한 응답 반환 (예: 500 Internal Server Error)
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send notification", e);
//        }
//    }
//}
//
//// NotificationRequest 클래스
//@Getter
//@Setter
//class NotificationRequest {
//    private String deviceToken;
//    private String title;
//    private String body;
//    private String code; // 추가된 코드 필드
//}
