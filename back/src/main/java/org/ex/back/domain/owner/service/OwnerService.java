package org.ex.back.domain.owner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.owner.dto.OwnerLoginRequestDto;
import org.ex.back.domain.owner.dto.OwnerSignUpRequestDto;
import org.ex.back.global.jwt.TokenResponseDto;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.owner.repository.OwnerRepository;
import org.ex.back.global.enumclass.Role;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.ex.back.global.jwt.JwtTokenProvider;
import org.ex.back.global.jwt.RefreshTokenEntity;
import org.ex.back.global.jwt.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;

    // 회원가입
    public void signUp(OwnerSignUpRequestDto request) {

        // id 중복 검사
        if (ownerRepository.findById(request.getId()).isPresent()){
            throw new CustomException(ErrorCode.HAS_ID);
        }

        // 비밀번호 암호화 후 DB에 저장
        OwnerEntity owner = request.toEntity();
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        ownerRepository.save(owner);
    }

    // 로그인
    public TokenResponseDto login(OwnerLoginRequestDto request) throws Exception {

        // ID 조회 후 비밀번호 검사
        OwnerEntity owner = ownerRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다."));

        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return issueToken(owner.getOwner_pk());
    }

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
        Integer ownerPk = jwtTokenProvider.getPkFromJwtToken(refreshToken);
        Optional<RefreshTokenEntity> opToken = tokenRepository.findByOwnerPk(ownerPk);
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
        return issueToken(ownerPk);
    }

    private TokenResponseDto issueToken(Integer ownerPk) {
        // access token, refresh token 발급
        String accessToken = jwtTokenProvider.createAccessToken(ownerPk, Role.ROLE_OWNER.name());
        String refreshToken = jwtTokenProvider.createRefreshToken(ownerPk, Role.ROLE_OWNER.name());

        // refreshToken DB에 저장
        RefreshTokenEntity newToken = RefreshTokenEntity.builder()
                .ownerPk(ownerPk)
                .token(refreshToken)
                .build();

        Optional<RefreshTokenEntity> optionalToken = tokenRepository.findByOwnerPk(ownerPk);
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

}
