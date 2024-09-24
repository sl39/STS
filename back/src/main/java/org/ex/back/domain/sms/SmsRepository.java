package org.ex.back.domain.sms;

import org.ex.back.domain.sms.model.SMSEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsRepository extends JpaRepository<SMSEntity, String> {
    //리스트 만들어서 조회
    List<SMSEntity> findByExpirationTimeBefore(LocalDateTime expirationTime);
    
    //5분마다 삭제
    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);

    SMSEntity findByPhoneNum(String phoneNum);
}
