package org.ex.back.domain.sms.Service;

import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.ex.back.domain.sms.Util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KakaoService {

    private final SmsUtil messageUtil;

    @Autowired
    public KakaoService(SmsUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    // 주문 생성 시 카카오 알림톡 전송
    public SingleMessageSentResponse sendOrderKakaoMessage(
            String phoneNum,
            String variable1,
            Integer variable2,
            LocalDateTime variable3) {
        return messageUtil.sendOrderKakao(phoneNum, variable1, variable2, variable3);
    }

    // 웨이팅 알림톡 전송
    public SingleMessageSentResponse sendWaitKakaoMessage(String phoneNum) {
        return messageUtil.sendWaitKakao(phoneNum);
    }
}