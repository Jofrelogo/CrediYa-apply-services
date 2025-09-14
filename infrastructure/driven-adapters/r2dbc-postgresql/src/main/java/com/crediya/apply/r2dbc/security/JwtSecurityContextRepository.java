package com.crediya.apply.r2dbc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Loads SecurityContext from Authorization header using JwtAuthenticationManager.
 * We implement it but we will use NoOpServerSecurityContextRepository in filter to keep stateless behavior.
 */
@Component
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private static final Logger log = LoggerFactory.getLogger(JwtSecurityContextRepository.class);

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        // stateless JWT -> do not store
        log.trace("[JwtSecurityContextRepository] save() called - stateless, no-op");
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        // We don't use this path because we use AuthenticationWebFilter with NoOpServerSecurityContextRepository.
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.trace("[JwtSecurityContextRepository] load() called, headerPresent={}", auth != null);
        return Mono.empty();
    }
}


