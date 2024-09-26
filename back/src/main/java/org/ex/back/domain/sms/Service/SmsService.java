package org.ex.back.domain.sms.Service;


import org.ex.back.domain.sms.DTO.SmsRequestDTO;
import org.ex.back.domain.sms.Repository.SmsRepository;
import org.ex.back.domain.sms.Util.SmsUtil;
import org.ex.back.domain.sms.model.SMSEntity;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsService {

    private final SmsUtil smsUtil;
    private final SmsRepository smsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public SmsService(SmsUtil smsUtil, SmsRepository smsRepository) {
        this.smsUtil = smsUtil;
        this.smsRepository = smsRepository;
    }

    // 인증번호 전송
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

    //인증번호 확인
    public String verifyCertificationCode(String phoneNum, String inputCode) {
        // 전화번호로 저장된 인증코드 조회
        SMSEntity smsEntity = smsRepository.findByPhoneNum(phoneNum); // 전화번호로 조회

        //전화번호 존재하지않을 때
        if (smsEntity == null) {
            return "전화번호를 확인하세요.";
        }
        //인증번호 불일치
        if (!smsEntity.getCertificationCode().equals(inputCode)) {
            return  "인증번호가 일치하지 않습니다.";
        }
        //인증 성공
        smsEntity.setCertifiedState(true);
        smsRepository.save(smsEntity);
        return "인증에 성공했습니다.";
    }
    
    // 인증번호 확인 후 전화번호 저장
    public String registerUser(Integer userPk, String phoneNum) {
        SMSEntity smsEntity = smsRepository.findByPhoneNum(phoneNum);

        if (smsEntity != null && smsEntity.getCertifiedState()) {
            // user_pk에 해당하는 사용자 조회
            UserEntity existingUser = userRepository.findById(userPk).orElse(null);

            // 기존 사용자가 존재하고 전화번호가 null인 경우만 업데이트
            if (existingUser != null && existingUser.getPhone() == null) {
                existingUser.setPhone(phoneNum);
                userRepository.save(existingUser);
                return "사용자의 전화번호가 업데이트되었습니다.";
            } else if (existingUser != null) {
                return "사용자의 전화번호는 이미 등록되어 있습니다."; // 전화번호가 null이 아닌 경우
            } else {
                return "해당 사용자가 존재하지 않습니다."; // 사용자 없을 때 메시지
            }
        }
        return "인증이 완료되지 않았습니다."; // 인증이 완료되지 않은 경우
    }


    //스케쥴 5분마다 삭제
//    @Transactional
//    @Scheduled(fixedRate = 60000) // 1분마다 실행
//    public void deleteExpiredSms() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // 만료된 db 조회
//        List<SMSEntity> expiredSms = smsRepository.findByExpirationTimeBefore(now);
//
//        if (!expiredSms.isEmpty()) {
//            // 만료된 db 삭제
//            smsRepository.deleteByExpirationTimeBefore(now);
//            System.out.println("만료된 인증코드가 삭제되었습니다: " + expiredSms.size() + "개");
//        }
//    }
}

