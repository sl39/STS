package org.ex.back.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.cart.model.CartEntity;
import org.ex.back.domain.cart.repository.CartRepository;
import org.ex.back.domain.owner.dto.TokenResponseDto;
import org.ex.back.domain.user.model.UserEntity;
import org.ex.back.domain.user.repository.UserRepository;
import org.ex.back.global.enumclass.Role;
import org.ex.back.global.jwt.JwtTokenProvider;
import org.ex.back.global.jwt.RefreshTokenEntity;
import org.ex.back.global.jwt.RefreshTokenRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    /*
        Oauth2 로그인 후 (회원가입 처리)
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // Spring oauth2-client 에서 authorization code, access token 해결
        // 이 메서드는 사용자 정보 요청의 응답을 받음

        // 1. Oauth2 로그인 유저 정보 추출
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oauth2 attributes : {}", oAuth2User.getAttributes());
        log.info("oauth2 clientRegistration [{}] ", userRequest.getClientRegistration());
        log.info("oauth2 accessToken [{}]", userRequest.getAccessToken().getTokenValue());

        // 2. 로그인 provider 추출 (kakao, naver, google)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        log.info("oauth2 provider : {}", provider);

        // 3. 사용자 정보를 매핑
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(provider, oAuth2User.getAttributes());
        log.info("oauth2 userInfo : {}", oAuth2UserInfo.toString());

        // 4. UserEntity 조회해보고 없으면 DB에 저장 (회원가입)
        UserEntity userEntity = userRepository.findBySocialId(oAuth2UserInfo.getSocialId())
                .orElseGet(() -> userRepository.save(oAuth2UserInfo.toEntity()));
        log.info("oauth2 userEntity : {}", userEntity);

        // 5. UserEntity 생성과 함께 장바구니 생성
        cartRepository.findByUser(userEntity).orElseGet(() ->
                        cartRepository.save(CartEntity.builder().user(userEntity).build()));

        return new CustomUserDetails(userEntity, oAuth2User.getAttributes());
    }
}
