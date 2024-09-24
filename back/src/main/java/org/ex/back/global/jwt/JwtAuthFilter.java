package org.ex.back.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.owner.service.CustomOwnerDetailsService;
import org.ex.back.domain.user.service.CustomOAuth2UserService;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOwnerDetailsService ownerDetailsService;
    private final CustomOAuth2UserService userDetailsService;
    private final BlacklistRepository blacklistRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            log.info("헤더에 Authorization가 없거나 키값이 Bearer 로 시작하지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String[] authElements = header.split(" ");

        try {
            String jwt = authElements[1];
            log.info("jwt 추출 : {}", jwt);

            if (jwtTokenProvider.validateJwtToken(jwt)) {
                log.info("jwt 검증 통과");

                // BlackList 확인
                Optional<BlacklistEntity> entity = blacklistRepository.findByToken(jwt);
                if (entity.isPresent()) {
                    throw new CustomException(ErrorCode.RELOGIN_REQUIRED);
                }

                //User, Owner 두 가지 Role 고려
                UserDetails userDetails;

                String role = jwtTokenProvider.getRoleFromJwtToken(jwt);
                if (role.equals("ROLE_OWNER")) {

                    Integer ownerPk = jwtTokenProvider.getPkFromJwtToken(jwt);
                    log.info("ownerPk 추출 : {}", ownerPk);
                    userDetails = ownerDetailsService.loadUserByPk(ownerPk);
                    log.info("UserDetails 추출 : {}", userDetails);

                } else if (role.equals("ROLE_USER")) {

                    Integer userPk = jwtTokenProvider.getPkFromJwtToken(jwt);
                    log.info("userPk 추출 : {}", userPk);
                    userDetails = userDetailsService.loadUserByPk(userPk);
                    log.info("UserDetails 추출");
                } else {

                    log.error("Role에 대한 정보가 없습니다.");
                    throw new CustomException(ErrorCode.RELOGIN_REQUIRED);
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            throw new RuntimeException("문제가 발생했습니다 : " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
