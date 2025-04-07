package com.wimir.bae.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // 의존성 주입(DI)
    // 1. 객체를 직접 생성하지 않고, 필요한 객체를 미리 만들어서 주입 -> 필요한 클래스에서 @Autowired나 생성자 주입으로 쉽게 가져다 쓸 수 있음.
    // 2. new BCryptPasswordEncoder()를 여기저기서 직접 생성하는 대신, 하나의 설정만 관리하면 됨.(재사용성, 일관성)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
