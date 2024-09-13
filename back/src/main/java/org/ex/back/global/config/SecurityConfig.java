package org.ex.back.global.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.ex.back.domain.owner.service.OwnerService;
import org.ex.back.global.enumclass.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // HTTP 요청에 대한 보안 설정 구성
        http.csrf(AbstractHttpConfigurer::disable)
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebSecurityCustomizer configure(){
//        return (web) -> web.ignoring()
//                .requestMatchers("/static/**"); // 이 경로에 대해서는 보안 필터 무시 - 필요하면 주석 해제
//    }

}
