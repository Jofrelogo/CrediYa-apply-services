package com.crediya.apply.api;

import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.dto.ApplyResponseDTO;
import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.common.PageQuery;
import com.crediya.apply.model.common.PageResponse;
import com.crediya.apply.model.jwt.JwtProvider;
import com.crediya.apply.usecase.apply.ApplyUseCase;
import com.crediya.apply.usecase.apply.ListApplyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;
import java.util.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HandlerTest {

    private ApplyUseCase applyUseCase;
    private ListApplyUseCase listApplyUseCase;
    private Validator validator;
    private JwtProvider jwtProvider;
    private Handler handler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        applyUseCase = mock(ApplyUseCase.class);
        listApplyUseCase = mock(ListApplyUseCase.class);
        validator = mock(Validator.class);
        jwtProvider = mock(JwtProvider.class);

        handler = new Handler(applyUseCase, listApplyUseCase, validator, jwtProvider);

        webTestClient = WebTestClient.bindToRouterFunction(
                RouterFunctions.route()
                        .POST("/api/v1/apply", handler::createApply)
                        .GET("/api/v1/apply", handler::listForReview)
                        .build()
        ).build();
    }

    @Test
    void createApply_shouldReturnUnauthorizedWhenMissingToken() {
        webTestClient.post().uri("/api/v1/apply")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(String.class)
                .isEqualTo("{\"message\":\"Missing token\"}");
    }

    @Test
    void createApply_shouldReturnForbiddenWhenRoleNotClient() {
        when(jwtProvider.getDniFromToken(anyString())).thenReturn("123");
        when(jwtProvider.getRoleFromToken(anyString())).thenReturn("ADVISOR");

        ApplyRequestDTO applyRequestDTO = new ApplyRequestDTO();
        applyRequestDTO.setDni("123");

        webTestClient.post().uri("/api/v1/apply")
                .header("Authorization", "Bearer token123")
                .bodyValue(applyRequestDTO)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void createApply_shouldReturnUnauthorizedWhenDniMismatch() {
        when(jwtProvider.getDniFromToken(anyString())).thenReturn("111");
        when(jwtProvider.getRoleFromToken(anyString())).thenReturn("CLIENTE");
        ApplyRequestDTO applyRequestDTO = new ApplyRequestDTO();
        applyRequestDTO.setDni("222");

        webTestClient.post().uri("/api/v1/apply")
                .header("Authorization", "Bearer token123")
                .bodyValue(applyRequestDTO)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void createApply_shouldThrowConstraintViolationException() {
        when(jwtProvider.getDniFromToken(anyString())).thenReturn("111");
        when(jwtProvider.getRoleFromToken(anyString())).thenReturn("CLIENTE");
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("111");

        Set<ConstraintViolation<ApplyRequestDTO>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(dto)).thenReturn(violations);

        webTestClient.post().uri("/api/v1/apply")
                .header("Authorization", "Bearer token123")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is5xxServerError(); // ConstraintViolation lanza excepción
    }

    @Test
    void createApply_shouldSaveApplySuccessfully() {
        when(jwtProvider.getDniFromToken(anyString())).thenReturn("111");
        when(jwtProvider.getRoleFromToken(anyString())).thenReturn("CLIENTE");

        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("111");
        when(validator.validate(dto)).thenReturn(Collections.emptySet());

        Apply domain = new Apply(); domain.setDni("111");
        ApplyResponseDTO responseDTO = new ApplyResponseDTO();
        responseDTO.setDni("111");
        when(applyUseCase.saveApply(any())).thenReturn(Mono.just(domain));

        webTestClient.post().uri("/api/v1/apply")
                .header("Authorization", "Bearer token123")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.dni").isEqualTo("111");
    }

    @Test
    void listForReview_shouldReturnPageResponse() {
        PageResponse<Apply> pageResponse = new PageResponse<>(Collections.emptyList(), 0, 10, 0);
        when(listApplyUseCase.listForReviewPaged(anyList(), any(PageQuery.class)))
                .thenReturn(Mono.just(pageResponse));

        webTestClient.get().uri("/api/v1/apply?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalElements").isEqualTo(0);
    }

    @Test
    void listForReview_shouldHandleErrorGracefully() {
        when(listApplyUseCase.listForReviewPaged(anyList(), any(PageQuery.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        webTestClient.get().uri("/api/v1/apply?page=0&size=10")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Internal Server Error")
                .jsonPath("$.message").isEqualTo("Internal error retrieving applications");
    }
}
