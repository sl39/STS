package org.ex.back.global.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.global.enumclass.Role;
import org.ex.back.global.jwt.JwtTokenProvider;
import org.ex.back.global.jwt.RefreshTokenEntity;
import org.ex.back.global.jwt.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Value("${oauth2.success.redirect.url}")
    private String url;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException
    {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Integer userPk = Integer.parseInt(oauth2User.getName());

        // access token, refresh token 발급
        String accessToken = jwtTokenProvider.createAccessToken(userPk, Role.ROLE_USER.name());
        String refreshToken = jwtTokenProvider.createRefreshToken(userPk, Role.ROLE_USER.name());
        log.info("access token : {}", accessToken);
        log.info("refresh token : {}", refreshToken);

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

        // Response Header 설정
        response.addHeader("Set-Cookie", createCookie("accessToken", accessToken).toString());
        response.addHeader("Set-Cookie", createCookie("refreshToken", refreshToken).toString());

        redirectStrategy.sendRedirect(request, response, url);
    }

    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .sameSite("")
                .path("/")
                .build();
    }
}
