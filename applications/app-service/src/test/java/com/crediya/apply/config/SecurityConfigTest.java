package com.crediya.apply.config;

import com.crediya.apply.model.jwt.JwtProvider;
import com.crediya.apply.r2dbc.security.JwtProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches("myPassword", encoder.encode("myPassword")),
                "PasswordEncoder should correctly encode and match passwords");
    }

    @Test
    void testJwtProviderBean() {
        JwtProperties props = new JwtProperties();
        props.setSecret("mysecretkeymysecretkeymysecretkey12345"); // mínimo 32 chars
        props.setExpiration(3600000L);

        JwtProvider jwtProvider = securityConfig.jwtProvider(props);

        assertNotNull(jwtProvider);
        assertTrue(jwtProvider instanceof JwtProviderImpl,
                "JwtProvider bean should be an instance of JwtProviderImpl");
    }
}

