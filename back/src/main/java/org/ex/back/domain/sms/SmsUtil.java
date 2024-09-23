package org.ex.back.domain.sms;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromNumber}")
    private String fromNumber;

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
}
