package org.ex.back.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.owner.service.CustomOwnerDetailsService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOwnerDetailsService userDetailsService;

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

                Integer ownerPk = Integer.parseInt(jwtTokenProvider.getPkFromJwtToken(jwt));
                log.info("ownerPk 추출 : {}", ownerPk);

                UserDetails userDetails = userDetailsService.loadUserByPk(ownerPk);
                log.info("UserDetails 추출 : {}", userDetails);

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
