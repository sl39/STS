package org.ex.back.domain.owner.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.owner.dto.CheckBRNRequestDto;
import org.ex.back.domain.owner.dto.CheckBRNResponseDto;
import org.ex.back.domain.owner.dto.OwnerLoginRequestDto;
import org.ex.back.domain.owner.dto.OwnerSignUpRequestDto;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.owner.service.OwnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/auth/seller")
public class OwnerController {

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    private final OwnerService ownerService;


    // 사업자 번호 조회
    @PostMapping("/brn")
    public ResponseEntity<CheckBRNResponseDto> checkBRN(@RequestBody CheckBRNRequestDto request) throws Exception {

        String BRN = request.getBrn().replace("-", "");
        log.info(BRN);

        String url = "https://api.odcloud.kr/api/nts-businessman/v1/status?" +
                "serviceKey=" + serviceKey +
                "&returnType=" + "json";

        // JsonObject 생성
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(BRN);
        jsonObject.add("b_no", jsonArray);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        // POST 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new Exception(responseEntity.getStatusCode() + ": 사업자등록번호를 조회할 수 없습니다.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody()).get("data").get(0);
        CheckBRNResponseDto dto = validateBRN(jsonNode);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private CheckBRNResponseDto validateBRN(JsonNode jsonNode) {
        String tax_type = jsonNode.get("tax_type").asText();
        String b_stt = jsonNode.get("b_stt").asText();

        log.info("tax_type: " + tax_type + ", b_stmt: " + b_stt);

        boolean isValid = false;
        String info;

        if(tax_type.equals("국세청에 등록되지 않은 사업자등록번호입니다.")) {
            info = "국세청에 등록되지 않은 사업자등록번호입니다.";
        } else {
            if(b_stt.equals("계속사업자")){
                isValid = true;
                info = "유효한 사업자등록번호입니다.";
            } else {
                info = "사업자등록번호가 휴업 및 폐업 상태입니다.";
            }
        }

        return CheckBRNResponseDto.builder().isValid(isValid).info(info).build();
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody OwnerSignUpRequestDto request) throws Exception {
        ownerService.signUp(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody OwnerLoginRequestDto request) throws Exception {
        return new ResponseEntity<>(ownerService.login(request), HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, @AuthenticationPrincipal OwnerEntity owner){
        String token = jwtTokenProvider.resolveAccessToken(request);
        ownerService.logout(token, owner);

        return new ResponseEntity(HttpStatus.OK);
    }
    */

    // 추후에...
//    @PostMapping("/sendTempPw")
//    @PostMapping("/resetPw")
}
