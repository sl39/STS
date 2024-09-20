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

        // access token, refresh token 발급
        String accessToken = jwtTokenProvider.createAccessToken(owner.getOwner_pk(), Role.ROLE_OWNER.name());
        String refreshToken = jwtTokenProvider.createRefreshToken(owner.getOwner_pk(), Role.ROLE_OWNER.name());

        // refreshToken DB에 저장
        RefreshTokenEntity newToken = RefreshTokenEntity.builder()
                .ownerPk(owner.getOwner_pk())
                .token(refreshToken)
                .build();

        Optional<RefreshTokenEntity> optionalToken = tokenRepository.findByOwnerPk(owner.getOwner_pk());
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

    // 엑세스 토큰 재발급


    // 로그아웃
}
