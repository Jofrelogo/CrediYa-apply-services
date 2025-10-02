package com.crediya.apply.consumer;

import com.crediya.apply.model.jwt.JwtProvider;
import com.crediya.apply.model.user.User;
import com.crediya.apply.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRestConsumer  implements UserRepository {

    private final WebClient.Builder webClientBuilder;
    private final JwtProvider jwtProvider;

    @Override
    public Mono<User> getUserByEmail(String token) {
        String emailFromToken = jwtProvider.getEmailFromToken(token);

        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users/by-email")
                        .queryParam("email", emailFromToken)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJsb3MzQHVuby5jb20iLCJkbmkiOiIzMzMzMzMiLCJyb2xlIjoiQVNFU09SIiwiaWF0IjoxNzU5MzY2MDU5LCJleHAiOjE3NTkzNjk2NTl9.GVq0x9pt5PbnyGBt7bsnDZDIqf7x318gkbFceXHqLTM") // 👈 Header dinámico
                .retrieve()
                .bodyToMono(User.class);
    }
}
