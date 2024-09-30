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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

//            if (header == null || !header.startsWith("Bearer ")) {
//                log.info("헤더에 엑세스 토큰이 없습니다.");
//
//                if (request.getRequestURI().equals("/")) {
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//            }

            String jwt = header.split(" ")[1];
            log.info("jwt 추출 : {}", jwt);

            jwtTokenProvider.validateJwtToken(jwt);

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
            ErrorResponseEntity entity = ErrorResponseEntity.toErrorResponseEntity(ErrorCode.TOKEN_NOT_VALID);
            response.getWriter().write(new ObjectMapper().writeValueAsString(entity));

        } catch (CustomException e) {
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json; charset=UTF-8");
            ErrorResponseEntity entity = ErrorResponseEntity.toErrorResponseEntity(e.getErrorCode());
            response.getWriter().write(new ObjectMapper().writeValueAsString(entity));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePathList = {
                "/", "/index.html", // 소셜로그인 테스트 페이지
                "/api/auth/**", // 인증 관련 api
                "/api/store/user/**", // 사용자 매장 조회 및 검색 api
                "/api/cart/nonuser/**", // 비회원 장바구니 CRUD api
                "/api/order/**", // 주문번호로 주문내역 조회 api
                "/api/sms/send", // 전화번호 인증 api
                "/api/sms/verify", // 전화번호 인증 확인 api
                "/api/store/*/menu", // 가게 메뉴리스트 조회 api
                "/api/menu/*/menu" // 메뉴 상세 조회 api
        };

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String path = request.getServletPath();

        for(String excludePath : excludePathList) {
            if(antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }

        return false;
    }
}