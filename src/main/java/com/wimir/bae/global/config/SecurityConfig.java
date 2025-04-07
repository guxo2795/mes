package com.wimir.bae.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // Postman 요청 위해 비활성화
                .formLogin().disable() // 로그인 폼 안 쓸 경우 비활성화
                .httpBasic().disable() // 기본 인증 비활성화 (JWT 등 사용할 경우)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                );

        return http.build();
    }

    // 의존성 주입(DI)
    // 1. 객체를 직접 생성하지 않고, 필요한 객체를 미리 만들어서 주입 -> 필요한 클래스에서 @Autowired나 생성자 주입으로 쉽게 가져다 쓸 수 있음.
    // 2. new BCryptPasswordEncoder()를 여기저기서 직접 생성하는 대신, 하나의 설정만 관리하면 됨.(재사용성, 일관성)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
