package org.ex.back.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {

    private RefreshTokenRepository tokenRepository;

    /*
            해야할 일 : DB에 저장된 RefreshToken 삭제!
     */

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {


        // accessToken
    }
}
