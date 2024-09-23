package org.ex.back.domain.sms;


import org.ex.back.domain.sms.model.SMSEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsService {

    private final SmsUtil smsUtil;
    private final SmsRepository smsRepository;

    @Autowired
    public SmsService(SmsUtil smsUtil, SmsRepository smsRepository) {
        this.smsUtil = smsUtil;
        this.smsRepository = smsRepository;
    }

    // SMS 전송
    public void sendSms(SmsRequestDTO smsRequestDto) {
        // 수신할 전화번호 가져오기
        String phoneNum = smsRequestDto.getPhoneNum();

        // 인증번호 생성
        String certificationCode = Integer.toString((int)(Math.random() * (9999 - 1000 + 1)) + 1000);

        // SMS 전송
        try {
            smsUtil.sendSMS(phoneNum, certificationCode);
            System.out.println("문자가 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            System.out.println("SMS 전송 실패: " + e.getMessage());
        }

        //sms 객체 생성
        SMSEntity smsEntity = new SMSEntity();
        smsEntity.setPhoneNum(phoneNum);
        smsEntity.setCertificationCode(certificationCode);
        smsEntity.setCreatedAt(LocalDateTime.now());
        smsEntity.setCertifiedState(false);
        smsEntity.setExpirationTime(LocalDateTime.now().plusMinutes(5)); //5분 후 삭제



        //db 저장
        try {
            smsRepository.save(smsEntity);
            System.out.println("정보가 저장되었습니다.");
        } catch (Exception e) {
            System.out.println("저장실패 : " + e.getMessage());
        }
    }

    //스케쥴 5분마다 삭제
    @Transactional
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void deleteExpiredSms() {
        LocalDateTime now = LocalDateTime.now();

        // 만료된 db 조회
        List<SMSEntity> expiredSms = smsRepository.findByExpirationTimeBefore(now);

        if (!expiredSms.isEmpty()) {
            // 만료된 db 삭제
            smsRepository.deleteByExpirationTimeBefore(now);
            System.out.println("만료된 인증코드가 삭제되었습니다: " + expiredSms.size() + "개");
        }
    }
}

