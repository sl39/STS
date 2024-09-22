package org.ex.back.domain.owner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.owner.dto.OwnerLoginRequestDto;
import org.ex.back.domain.owner.dto.OwnerSignUpRequestDto;
import org.ex.back.domain.owner.dto.TokenResponseDto;
import org.ex.back.domain.owner.model.OwnerEntity;
import org.ex.back.domain.owner.repository.OwnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class OwnerService {
/*
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final RedisTemplate redisTemplate;

    // 회원가입
    public Integer signUp(OwnerSignUpRequestDto request) throws Exception {

        // id 중복 검사
        if (ownerRepository.findById(request.getId()).isPresent()){
            throw new RuntimeException("동일한 ID가 이미 존재합니다.");
        }

        // 비밀번호 암호화 후 DB에 저장
        OwnerEntity owner = request.toEntity();
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        OwnerEntity encodedOwner = ownerRepository.save(owner);

        // 회원 장바구니 엔티티 만들어서 DB에 저장

        // 가게 엔티티 만들어서 DB 저장

        return encodedOwner.getOwner_pk();
    }

    // 로그인
    /*
    public TokenResponseDto login(OwnerLoginRequestDto request) throws Exception {
        // ID 조회 후 비밀번호 검사
        OwnerEntity owner = ownerRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다."));

        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 - token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // refreshToken DB에 저장


        return TokenResponseDto.builder()
                .grantType("Bearer")
                .jwtAccessToken(accessToken)
                .jwtRefreshToken(refreshToken)
                .build();
    }
    */

    // 로그아웃
}
