package com.example.socar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF(사이트 간 요청 위조) 보호 기능을 끕니다. CSRF는 주로 브라우저를 통해 로그인한 사용자를 보호하기 위해 사용됩니다.
        http.csrf(csrf -> csrf.disable())
                // 세션 관리 정책을 설정합니다. 여기서는 STATELESS(상태 없음)로 설정하여, 서버가 세션을 유지하지 않도록 합니다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 보안 규칙을 설정합니다.
                .authorizeHttpRequests(authorize -> authorize
                        // **로 끝나는 모든 경로는 인증 없이 접근할 수 있도록 허용합니다.
                        .requestMatchers("**").permitAll() // 예: "/home", "/about", 등
                        // 그 외의 모든 요청은 인증이 필요합니다.
                        .anyRequest().authenticated()
                );
        // 설정한 보안 구성을 기반으로 SecurityFilterChain을 생성하고 반환합니다.
        return http.build();
    }

}
