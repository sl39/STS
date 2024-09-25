package org.ex.back.domain.sms.Controller;

import org.ex.back.domain.sms.Service.KakaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @Autowired
    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    // 주문 생성 시 카카오 알림톡 전송
    @PostMapping("/sendOrder")
    public String sendOrderKakao(
            @RequestParam String phoneNum,
            @RequestParam String variable1,
            @RequestParam String variable2,
            @RequestParam String variable3) {
        kakaoService.sendOrderKakaoMessage(phoneNum, variable1, variable2, variable3);
        return "주문 알림톡이 전송되었습니다.";
    }

    // 웨이팅 알림톡 전송
    @PostMapping("/sendWait")
    public String sendWaitKakao(
            @RequestParam String phoneNum) {
        kakaoService.sendWaitKakaoMessage(phoneNum);
        return "웨이팅 알림톡이 전송되었습니다.";
    }
}
