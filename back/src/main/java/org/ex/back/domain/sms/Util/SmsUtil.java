package org.ex.back.domain.sms.Util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

@Component
public class SmsUtil {

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromNumber}")
    private String fromNumber;

    @Value("${coolsms.pfid}")
    private String pfid;

    @Value("${coolsms.orderTemplateid}")
    private String orderTemplateid;

    @Value("${coolsms.waitTemplateid}")
    private String waitTemplateid;

    //API
    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        // CoolSMS API 초기화
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    // SMS 전송
    public void sendSMS(String to, String certificationCode) {

        // 메시지 객체 생성
        Message message = new Message();
        message.setFrom(fromNumber); // 발신 번호 설정
        message.setTo(to); // 수신 번호 설정
        message.setText("본인확인 인증번호는 " + certificationCode + "입니다."); // 메시지 내용 설정

        // 메시지 전송
        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    // 주문 카카오 알림톡 전송
    public SingleMessageSentResponse sendOrderKakao(String to, String variable1, Integer variable2, String variable3) {
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(pfid); // 설정된 PF ID
        kakaoOption.setTemplateId(orderTemplateid); // 주문 템플릿 ID 설정

        // 변수 설정
        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{주문번호}", variable1);
        variables.put("#{금액}", variable2 + "원");
        variables.put("#{주문일자}", variable3);
        kakaoOption.setVariables(variables);

        Message message = new Message();
        message.setFrom(fromNumber); // 발신 번호 설정
        message.setTo(to); // 수신 번호 설정
        message.setKakaoOptions(kakaoOption); // 카카오 옵션 설정

        try {
            return this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            System.out.println("주문 알림톡 전송 실패: " + e.getMessage());
            return null;
        }
    }

    // 웨이팅 카카오 알림톡
    public SingleMessageSentResponse sendWaitKakao(String to) {
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(pfid); // 설정된 PF ID
        kakaoOption.setTemplateId(waitTemplateid); // 웨이팅 템플릿 ID 설정

        Message message = new Message();
        message.setFrom(fromNumber); // 발신 번호 설정
        message.setTo(to); // 수신 번호 설정
        message.setKakaoOptions(kakaoOption); // 카카오 옵션 설정

        try {
            return this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            System.out.println("웨이팅 알림톡 전송 실패: " + e.getMessage());
            return null;
        }
    }
}
