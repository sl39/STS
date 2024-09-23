package org.ex.back.domain.sms;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(@Autowired SmsService smsService) {
        this.smsService = smsService;
    }

    // SMS 전송 요청 처리
    @PostMapping("/send")
    public ResponseEntity<?> sendSMS(@RequestBody @Valid SmsRequestDTO smsRequestDTO) {

        // SmsService를 통해 SMS 전송
        smsService.sendSms(smsRequestDTO);

        // 성공적인 응답 반환
        return ResponseEntity.ok("문자를 전송했습니다.");
    }


}
