package com.crediya.apply.config;

import com.crediya.apply.r2dbc.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Central security configuration. Creates only the necessary beans here to avoid cycles.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityWebFluxConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityWebFluxConfig.class);

    private final JwtProviderImpl jwtProviderImpl;

    public SecurityWebFluxConfig(JwtProviderImpl jwtProviderImpl) {
        this.jwtProviderImpl = jwtProviderImpl;
    }

    public PasswordEncoder passwordEncoder() {
        log.info("[SecurityWebFluxConfig] Creating BCrypt PasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    // expose JwtAuthenticationManager bean
    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager() {
        log.info("[SecurityWebFluxConfig] Creating JwtAuthenticationManager bean");
        return new JwtAuthenticationManager(jwtProviderImpl);
    }

    // converter bean (uses jwtProviderImpl)
    @Bean
    public JwtServerAuthenticationConverter jwtServerAuthenticationConverter() {
        log.info("[SecurityWebFluxConfig] Creating JwtServerAuthenticationConverter bean");
        return new JwtServerAuthenticationConverter(jwtProviderImpl);
    }

    // AuthenticationWebFilter using the manager + converter
    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter(JwtAuthenticationManager authenticationManager,
                                                              JwtServerAuthenticationConverter converter) {
        log.info("[SecurityWebFluxConfig] Creating AuthenticationWebFilter");
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setServerAuthenticationConverter(converter);
        // stateless - do not persist SecurityContext
        filter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return filter;
    }

    // security chain
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthenticationWebFilter jwtAuthenticationWebFilter) {

        log.info("[SecurityWebFluxConfig] Building security filter chain");

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/apply/**").hasAnyRole("ADMIN", "ASESOR")
                        .pathMatchers(HttpMethod.POST, "/api/v1/apply/**").hasRole("CLIENTE")
                        .pathMatchers("/api/v1/login").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}

