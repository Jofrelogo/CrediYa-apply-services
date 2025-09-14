package com.crediya.apply.r2dbc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Extracts Bearer token, validates it and returns an Authentication with GrantedAuthority.
 * Returns Mono.empty() when no token or invalid.
 */
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final Logger log = LoggerFactory.getLogger(JwtServerAuthenticationConverter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProviderImpl jwtProvider; // concrete implementation type is fine here

    public JwtServerAuthenticationConverter(JwtProviderImpl jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .doOnNext(h -> log.debug("[JwtConverter] Authorization header detected"))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .doOnNext(token -> log.trace("[JwtConverter] Extracted token: {}", token))
                .flatMap(token -> {
                    if (!jwtProvider.validateToken(token)) {
                        log.warn("[JwtConverter] Token invalid");
                        return Mono.empty();
                    }
                    String email = jwtProvider.getEmailFromToken(token);
                    String role = jwtProvider.getRoleFromToken(token);
                    String roleAuthority = role == null ? "ROLE_UNKNOWN" : ("ROLE_" + role.toUpperCase());
                    log.info("[JwtConverter] Token valid for {}, role={}", email, role);

                    return Mono.<Authentication>just(
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    token,
                                    List.of(new SimpleGrantedAuthority(roleAuthority))
                            )
                    );
                })
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("[JwtConverter] No token found or token invalid")));
    }
}



