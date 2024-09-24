package org.ex.back.domain.sms.Controller;


import jakarta.validation.Valid;
import org.ex.back.domain.sms.DTO.SmsRequestDTO;
import org.ex.back.domain.sms.Service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 인증번호 확인 API
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String phoneNum, @RequestParam String inputCode) {
        String result = smsService.verifyCertificationCode(phoneNum, inputCode);

        if (result.equals("인증에 성공했습니다.")){
            return ResponseEntity.ok(result);
        } else if (result.equals("전화번호를 확인하세요.")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result); //404 에러
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); //400 에러
        }
    }

    // 전화번호 업데이트
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam Integer userPk, @RequestParam String phoneNum) {
        String result = smsService.registerUser(userPk, phoneNum);
        if (result.equals("사용자의 전화번호가 업데이트되었습니다.")) {
            return ResponseEntity.ok(result); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 에러
        }
    }

}
