package org.ex.back.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class LogoutService implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistRepository blacklistRepository;

    /*
            AccessToken은 BlackList에 추가, RefreshToken은 DB에서 삭제
     */

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // accessToken에서 pk, role 추출
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION); //Bearer 2fsd5...
        if (Objects.isNull(authorizationHeader)) throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        String accessToken = authorizationHeader.substring(7);

        Integer pk = jwtTokenProvider.getPkFromJwtToken(accessToken);
        String role = jwtTokenProvider.getRoleFromJwtToken(accessToken);

//        List<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();

        // accessToken를 BlackList에 추가
        blacklistRepository.findByToken(accessToken).orElseGet(() ->
                blacklistRepository.save(BlacklistEntity.builder().token(accessToken).build())
        );

        // refreshToken을 DB에서 삭제
        if (role.equals("ROLE_OWNER")) {
            refreshTokenRepository.findByOwnerPk(pk).ifPresent(refreshTokenRepository::delete);
        } else if (role.equals("ROLE_USER")) {
            refreshTokenRepository.findByUserPk(pk).ifPresent(refreshTokenRepository::delete);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
