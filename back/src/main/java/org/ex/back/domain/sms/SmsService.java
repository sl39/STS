package org.ex.back.domain.sms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final SmsUtil smsUtil;

    @Autowired
    public SmsService(SmsUtil smsUtil) {
        this.smsUtil = smsUtil;
    }

    // SMS 전송
    public void sendSms(SmsRequestDTO smsRequestDto) {
        // 수신할 전화번호 가져오기
        String phoneNum = smsRequestDto.getPhoneNum();

        // 인증번호 생성
        String certificationCode = Integer.toString((int)(Math.random() * (9999 - 1000 + 1)) + 1000);

        // SMS 전송
        smsUtil.sendSMS(phoneNum, certificationCode);
    }
}

