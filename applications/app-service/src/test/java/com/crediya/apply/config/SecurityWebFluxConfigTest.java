package com.crediya.apply.config;

import com.crediya.apply.r2dbc.security.JwtAuthenticationManager;
import com.crediya.apply.r2dbc.security.JwtProviderImpl;
import com.crediya.apply.r2dbc.security.JwtServerAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;


class SecurityWebFluxConfigTest {

    private JwtProviderImpl jwtProvider;
    private  SecurityWebFluxConfig config;


    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProviderImpl("mySecretKey1234567890mySecretKey1234567890", 3600000);
        config = new SecurityWebFluxConfig(jwtProvider);
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder.matches("mypassword", encoder.encode("mypassword")));
    }

    @Test
    void testJwtAuthenticationManagerBean() {
        JwtAuthenticationManager manager = config.jwtAuthenticationManager();
        assertNotNull(manager);
        assertTrue(manager instanceof ReactiveAuthenticationManager);
    }

    @Test
    void testJwtServerAuthenticationConverterBean() {
        JwtServerAuthenticationConverter converter = config.jwtServerAuthenticationConverter();
        assertNotNull(converter);
    }

    @Test
    void testJwtAuthenticationWebFilterBean() {
        JwtAuthenticationManager manager = config.jwtAuthenticationManager();
        JwtServerAuthenticationConverter converter = config.jwtServerAuthenticationConverter();

        AuthenticationWebFilter filter = config.jwtAuthenticationWebFilter(manager, converter);

        assertNotNull(filter);

        Object injectedConverter = ReflectionTestUtils.getField(filter, "authenticationConverter");
        assertEquals(converter, injectedConverter);
    }

    @Test
    void testSecurityWebFilterChainBean() {
        JwtAuthenticationManager manager = config.jwtAuthenticationManager();
        JwtServerAuthenticationConverter converter = config.jwtServerAuthenticationConverter();
        AuthenticationWebFilter filter = config.jwtAuthenticationWebFilter(manager, converter);

        ServerHttpSecurity http = ServerHttpSecurity.http();
        SecurityWebFilterChain chain = config.securityWebFilterChain(http, filter);

        assertNotNull(chain);
    }
}

