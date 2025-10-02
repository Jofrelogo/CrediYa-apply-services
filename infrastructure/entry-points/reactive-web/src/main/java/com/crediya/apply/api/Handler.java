package com.crediya.apply.api;

import com.crediya.apply.api.dto.ApplyDecisionRequestDTO;
import com.crediya.apply.api.dto.ApplyDecisionResponseDTO;
import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.mapper.ApplyMapper;
import com.crediya.apply.model.common.PageQuery;
import com.crediya.apply.model.jwt.JwtProvider;
import com.crediya.apply.usecase.apply.ApplyUseCase;
import com.crediya.apply.usecase.apply.DecideApplyUseCase;
import com.crediya.apply.usecase.apply.ListApplyUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ApplyUseCase applyUseCase;
    private final ListApplyUseCase listApplyUseCase;
    private final DecideApplyUseCase decideApplyUseCase;
    private final Validator validator;
    private final JwtProvider jwtProvider;
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> createApply(ServerRequest serverRequest) {
        return serverRequest.headers().header("Authorization").stream().findFirst()
                .map(authHeader -> authHeader.replace("Bearer ", ""))
                .map(token -> {
                    String dniFromToken = jwtProvider.getDniFromToken(token);
                    String role = jwtProvider.getRoleFromToken(token);

                    log.info("🔍 Validating apply creation: dni={}, role={}", dniFromToken, role);

                    if (!"CLIENTE".equals(role)) {
                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                .bodyValue("{\"message\":\"Only CLIENT role can create applies\"}");
                    }
                    return serverRequest.bodyToMono(ApplyRequestDTO.class)
                            .flatMap(dto -> {
                                log.info("📩 Request received to create apply: {}", dto.getDni());
                                if (!dniFromToken.equals(dto.getDni())) {
                                    return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                            .bodyValue("{\"message\":\"DNI mismatch\"}");
                                }
                                // ✅ Validación con Bean Validation
                                Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
                                if (!violations.isEmpty()) {
                                    log.warn("⚠️ Validation violations in createApply: {}", violations);
                                    throw new ConstraintViolationException(violations);
                                }

                                // ✅ Guardar usuario
                                return applyUseCase.saveApply(ApplyMapper.requestToDomain(dto), token)
                                        .doOnNext(user -> log.info("✅ Apply saved: {}", user.getDni()))
                                        .doOnError(err -> log.error("❌ Error saving apply {}", dto.getDni(), err))
                                        .map(ApplyMapper::domainToRespons)
                                        .flatMap(saved -> ServerResponse.ok().bodyValue(saved));
                            });
                })
                .orElse(ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue("{\"message\":\"Missing token\"}"));
    }

    public Mono<ServerResponse> listForReview(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("1"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        List<String> reviewStatuses = Arrays.asList(
                "PENDING_REVIEW",
                "REJECTED",
                "PENDING_MANUAL_REVIEW"
        );
        log.info("📄 Received request to list applications for manual review");
        log.info("📄 Listing applies for review page={}, size={}, statuses={}", page, size, reviewStatuses);

        PageQuery pageQuery = new PageQuery(page, size);

        return listApplyUseCase.listForReviewPaged(reviewStatuses, pageQuery)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .doOnError(e -> log.error("❌ Error retrieving applications for review", e))
                .onErrorResume(e -> {
                    log.error("🔥 Internal error in listForReview", e);
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "timestamp", Instant.now().toString(),
                                    "status", 500,
                                    "error", "Internal Server Error",
                                    "message", "Internal error retrieving applications"
                            ));
                });
    }

    public Mono<ServerResponse> decideApply(ServerRequest request) {
        return request.bodyToMono(ApplyDecisionRequestDTO.class)
                .flatMap(dto -> decideApplyUseCase.decideApply(dto.getApplyId(), dto.getDecision()))
                .map(apply -> ApplyDecisionResponseDTO.builder()
                        .applyId(apply.getDni())
                        .status(apply.getState())
                        .build()
                )
                .flatMap(responseDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDto))
                .onErrorResume(error -> {
                    log.error("Error in decideApply handler: {}", error.getMessage());
                    return ServerResponse.badRequest().bodyValue(Map.of("error", error.getMessage()));
                });
    }

}
