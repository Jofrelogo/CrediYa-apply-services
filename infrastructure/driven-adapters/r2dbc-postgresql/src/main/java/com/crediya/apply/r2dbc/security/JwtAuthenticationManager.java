package com.crediya.apply.r2dbc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * ReactiveAuthenticationManager that expects a UsernamePasswordAuthenticationToken
 * where the principal is the username/email and credentials is the token.
 */
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationManager.class);

    private final JwtProviderImpl jwtProvider;

    public JwtAuthenticationManager(JwtProviderImpl jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        // principal = email OR token depending on converter; we used email as principal in converter
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();

        log.debug("[JwtAuthManager] authenticate called, principal={}, credentialsPresent={}", principal, credentials != null);

        String token = credentials == null ? null : credentials.toString();

        if (token == null || !jwtProvider.validateToken(token)) {
            log.warn("[JwtAuthManager] No token or invalid token");
            return Mono.empty();
        }

        String email = jwtProvider.getEmailFromToken(token);
        String role = jwtProvider.getRoleFromToken(token);
        String dni = jwtProvider.getDniFromToken(token);
        String roleAuthority = role == null ? "ROLE_UNKNOWN" : ("ROLE_" + role.toUpperCase());

        log.info("[JwtAuthManager] Token valid -> dni={}, user={}, role={}", dni, email, roleAuthority);

        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        // 👇 Guardamos el DNI en los details
        auth.setDetails(dni);

        return Mono.just(auth);
    }
}


