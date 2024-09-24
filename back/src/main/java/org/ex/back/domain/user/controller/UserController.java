package org.ex.back.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.user.dto.UserPhoneDto;
import org.ex.back.domain.user.service.UserPrincipal;
import org.ex.back.domain.user.service.UserService;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.ex.back.global.jwt.TokenResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/auth/user")
public class UserController {

    private final UserService userService;

    // 전화번호 유무 체크 (= 인증 여부 체크)
    @GetMapping("/phone")
    public ResponseEntity<UserPhoneDto> getPhone(@AuthenticationPrincipal UserDetails userDetails) {

        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        Integer userPk = userPrincipal.getUserPk();
        return new ResponseEntity<>(userService.getPhone(userPk), HttpStatus.OK);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request) {

        // Header에 Token 있는지 검사
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION); //Bearer 2fsd5...
        String refreshTokenHeader = request.getHeader("Refresh-Token"); //2fsd5...
        if (Objects.isNull(authorizationHeader) || Objects.isNull(refreshTokenHeader)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        // Token 검증 후 새로운 토큰 발급
        String accessToken = authorizationHeader.substring(7);
        TokenResponseDto tokenDto = userService.reissueToken(accessToken, refreshTokenHeader);

        // 헤더에 생성한 token 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", createCookie("accessToken", tokenDto.getAccessToken()).toString());
        headers.add("Set-Cookie", createCookie("refreshToken", tokenDto.getRefreshToken()).toString());

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .sameSite("")
                .path("/")
                .build();
    }
}
