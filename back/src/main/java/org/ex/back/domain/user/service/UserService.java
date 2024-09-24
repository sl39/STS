package org.ex.back.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.user.dto.UserPhoneDto;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.user.repository.UserRepository;
import org.ex.back.global.enumclass.Role;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.ex.back.global.jwt.JwtTokenProvider;
import org.ex.back.global.jwt.RefreshTokenEntity;
import org.ex.back.global.jwt.RefreshTokenRepository;
import org.ex.back.global.jwt.TokenResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;

    // 엑세스 토큰 재발급
    public TokenResponseDto reissueToken(String accessToken, String refreshToken) {
        // 1. 요청 들어온 token의 유효성 검증 (expires)
        // accessToken은 만료되어 있어야 하고 refreshToken은 만료되지 않아야 함
        log.info("token 유효성 검증 시작");
//        테스트를 위해 주석처리
//        if(jwtTokenProvider.validateJwtToken(accessToken)){
//            log.error("access token이 아직 만료되지 않았습니다.");
//            throw new CustomException(ErrorCode.RELOGIN_REQUIRED);
//        }
        if(!jwtTokenProvider.validateJwtToken(refreshToken)){
            log.error("refresh token이 만료되었습니다.");
            throw new CustomException(ErrorCode.RELOGIN_REQUIRED);
        }
        log.info("token 유효성 검증 통과");

        // 2. DB에 저장된 refreshToken과 요청으로 온 refreshToken이 동일한지 확인
        log.info("token 정보 DB와 비교 시작");
        Integer userPk = Integer.parseInt(jwtTokenProvider.getPkFromJwtToken(refreshToken));
        Optional<RefreshTokenEntity> opToken = tokenRepository.findByUserPk(userPk);
        if(opToken.isPresent()){
            RefreshTokenEntity refreshTokenEntity = opToken.get();
            if(!refreshTokenEntity.getToken().equals(refreshToken)){
                log.error("DB에 저장된 refresh token과 값이 다릅니다.");
                throw new CustomException(ErrorCode.RELOGIN_REQUIRED);
            }
        } else {
            log.error("DB에 refresh token이 저장되어 있지 않습니다.");
            throw new CustomException(ErrorCode.RELOGIN_REQUIRED);
        }
        log.info("token 정보 DB와 비교 통과");

        // 3. token 새로 생성하고 RefreshToken 조회해서 엔티티 수정
        return issueToken(userPk);
    }

    private TokenResponseDto issueToken(Integer userPk) {
        // access token, refresh token 발급
        String accessToken = jwtTokenProvider.createAccessToken(userPk, Role.ROLE_USER.name());
        String refreshToken = jwtTokenProvider.createRefreshToken(userPk, Role.ROLE_USER.name());

        // refreshToken DB에 저장
        RefreshTokenEntity newToken = RefreshTokenEntity.builder()
                .userPk(userPk)
                .token(refreshToken)
                .build();

        Optional<RefreshTokenEntity> optionalToken = tokenRepository.findByUserPk(userPk);
        if(optionalToken.isPresent()) {
            RefreshTokenEntity oldToken = optionalToken.get();
            oldToken.setToken(refreshToken);
            tokenRepository.save(oldToken);
        } else {
            tokenRepository.save(newToken);
        }

        return TokenResponseDto.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserPhoneDto getPhone(Integer userPk) {

        // User 조회해서 phone 들어와 있는지 검사
        UserEntity user = userRepository.findById(userPk).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(user.getPhone() == null || user.getPhone().isEmpty() || user.getPhone().isBlank()){
            throw new CustomException(ErrorCode.PHONE_NUMBER_NOT_VERIFIED);
        }

        // dto로 변환해서 반환
        return UserPhoneDto.builder().phoneNumber(user.getPhone()).build();
    }
}
