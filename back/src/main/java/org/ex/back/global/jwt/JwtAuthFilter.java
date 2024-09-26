package org.ex.back.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
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
import org.ex.back.global.error.ErrorResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
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
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                log.info("헤더에 Authorization가 없거나 키값이 Bearer 로 시작하지 않습니다.");

                String requestURL = request.getRequestURI();
                if (requestURL.equals("/") || requestURL.startsWith("/api/auth/")) {
                    filterChain.doFilter(request, response); // 다음 필터로 진행
                    return;
                }

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // -> 토큰이 없기 때문에 401 로 넘기기
                return;
            }

            String jwt = header.split(" ")[1];
            log.info("jwt 추출 : {}", jwt);

            if(!jwtTokenProvider.validateJwtToken(jwt)) {
                throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
            }

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
                userDetails = ownerDetailsService.loadUserByPk(ownerPk);

            } else if (role.equals("ROLE_USER")) {
                Integer userPk = jwtTokenProvider.getPkFromJwtToken(jwt);
                userDetails = userDetailsService.loadUserByPk(userPk);

            } else {
                log.error("Role에 대한 정보가 없습니다.");
                throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (SignatureException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json; charset=UTF-8");
            ResponseEntity<ErrorResponseEntity> responseEntity = ErrorResponseEntity.toResponseEntity(ErrorCode.TOKEN_NOT_VALID);
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity));

        } catch (CustomException e) {
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json; charset=UTF-8");
            ResponseEntity<ErrorResponseEntity> responseEntity = ErrorResponseEntity.toResponseEntity(e.getErrorCode());
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity));
            //response.getWriter().flush();
        }
    }
}