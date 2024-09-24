package org.ex.back.global.config;

import lombok.RequiredArgsConstructor;
import org.ex.back.domain.user.service.CustomOAuth2UserService;
import org.ex.back.global.jwt.JwtAccessDeniedHandler;
import org.ex.back.global.jwt.JwtAuthFilter;
import org.ex.back.global.jwt.JwtAuthenticationEntryPoint;
import org.ex.back.global.jwt.LogoutService;
import org.ex.back.global.oauth.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final LogoutService logoutService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // HTTP 요청에 대한 보안 설정 구성
        http
                .cors(Customizer.withDefaults())

                // JWT 사용에 따른 설정
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 소셜로그인 설정
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint  -> userInfoEndpoint
                                .userService(customOAuth2UserService))
                                .successHandler(oAuth2SuccessHandler)
                )

                // 인증, 인가 실패 처리
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) //401
                        .accessDeniedHandler(jwtAccessDeniedHandler) //403
                )

                // 로그아웃 처리
                .logout(logoutConfig -> logoutConfig
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler((((request, response, authentication) ->
                                SecurityContextHolder.clearContext())))
                )

                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll()
                        //.requestMatchers("/api/auth/**").permitAll()
                        //.requestMatchers("/api/seller").hasRole("OWNER")
                        //.requestMatchers("/api/user").hasRole("USER")
                        //.anyRequest().authenticated() // 위에서 명시되지 않은 모든 요청은 인증된 사용자만 접근 가능
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
