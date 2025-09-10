package com.crediya.apply.config;


import com.crediya.apply.model.jwt.JwtProvider;
import com.crediya.apply.r2dbc.security.JwtProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityWebFluxConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("[SecurityWebFluxConfig] Creating BCrypt PasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProvider jwtProvider(JwtProperties jwtProperties) {
        return new JwtProviderImpl(jwtProperties.getSecret(), jwtProperties.getExpiration());
    }
}




