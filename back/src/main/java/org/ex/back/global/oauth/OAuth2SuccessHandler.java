package org.ex.back.global.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ex.back.domain.owner.dto.TokenResponseDto;
import org.ex.back.global.enumclass.Role;
import org.ex.back.global.jwt.JwtTokenProvider;
import org.ex.back.global.jwt.RefreshTokenEntity;
import org.ex.back.global.jwt.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;

    @Value("${oauth2.success.redirect.url}")
    private String url;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Integer userPk = Integer.parseInt(oauth2User.getName());

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

        // 리다이렉트
        String targetUrl = url +
                "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8) +
                "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        response.sendRedirect(targetUrl);
    }
}
