package com.crediya.apply.r2dbc.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderImplTest {

    private JwtProviderImpl jwtProvider;

    @BeforeEach
    void setUp() {
        // Secret debe ser suficientemente largo para HS256
        String secret = "my-super-secret-key-that-is-very-long-for-jwt-256";
        long expirationMillis = 2000; // 2 segundos
        jwtProvider = new JwtProviderImpl(secret, expirationMillis);
        jwtProvider.init(); // inicializa SecretKey
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtProvider.generateToken("12345", "test@example.com", "CLIENTE");

        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));

        String email = jwtProvider.getEmailFromToken(token);
        String dni = jwtProvider.getDniFromToken(token);
        String role = jwtProvider.getRoleFromToken(token);

        assertEquals("test@example.com", email);
        assertEquals("12345", dni);
        assertEquals("CLIENTE", role);
    }

    @Test
    void testValidateToken_invalidToken_returnsFalse() {
        String fakeToken = "invalid.token.value";
        assertFalse(jwtProvider.validateToken(fakeToken));
    }

    @Test
    void testGetClaimsAfterExpiration() throws InterruptedException {
        // Token con expiración corta (2s definida en setUp)
        String token = jwtProvider.generateToken("99999", "expired@example.com", "ADVISOR");

        // esperamos más de 2s
        Thread.sleep(2500);

        assertFalse(jwtProvider.validateToken(token));
    }

    @Test
    void testInitCreatesSecretKey() {
        String secret = "another-secret-key-that-is-long-enough-for-hmac-sha256";
        JwtProviderImpl customProvider = new JwtProviderImpl(secret, 10000);
        customProvider.init();

        // Probamos que genera token válido
        String token = customProvider.generateToken("54321", "init@example.com", "ADMIN");
        assertTrue(customProvider.validateToken(token));
    }
}
