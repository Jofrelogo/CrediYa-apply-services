package com.crediya.apply.r2dbc.security;

import com.crediya.apply.model.jwt.JwtProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Infrastructure implementation of JwtProvider.
 * Uses a SecretKey built from a strong 'jwt.secret' property.
 */
@Component
@Primary
public class JwtProviderImpl implements JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProviderImpl.class);

    private final String secret;
    private final long expirationMillis;
    private SecretKey secretKey;

    public JwtProviderImpl(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.expiration:3600000}") long expirationMillis) {
        this.secret = secret;
        this.expirationMillis = expirationMillis;
    }

    @PostConstruct
    public void init() {
        // ensure secret is long enough; Keys.hmacShaKeyFor will throw if too short
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        log.info("[JwtProviderImpl] SecretKey initialized (HS256). Expiration={}ms", expirationMillis);
    }

    @Override
    public String generateToken(String dni, String email, String role) {
        log.debug("[JwtProviderImpl] Generating token for email={} role={}", email, role);
        String token = Jwts.builder()
                .setSubject(email)
                .claim("dni", dni)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        log.trace("[JwtProviderImpl] Token generated: {}", token);
        return token;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            log.debug("[JwtProviderImpl] Token validated");
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("[JwtProviderImpl] Token expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("[JwtProviderImpl] Unsupported JWT: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("[JwtProviderImpl] Malformed JWT: {}", ex.getMessage());
        } catch (SignatureException ex) {
            log.warn("[JwtProviderImpl] Invalid JWT signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("[JwtProviderImpl] Illegal arg token: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("[JwtProviderImpl] Unexpected error validating token", ex);
        }
        return false;
    }

    @Override
    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        log.debug("[JwtProviderImpl] Extracted email from token: {}", email);
        return email;
    }

    @Override
    public String getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        Object role = claims.get("role");
        String r = role == null ? null : role.toString();
        log.debug("[JwtProviderImpl] Extracted role from token: {}", r);
        return r;
    }

    @Override
    public String getDniFromToken(String token) {
        Claims claims = getClaims(token);
        Object dni = claims.get("dni");
        String r = dni == null ? null : dni.toString();
        log.debug("[JwtProviderImpl] Extracted dni from token: {}", r);
        return r;
    }


    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


