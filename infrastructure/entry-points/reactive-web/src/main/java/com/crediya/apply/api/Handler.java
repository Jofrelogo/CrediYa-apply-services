package com.crediya.apply.api;

import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.mapper.ApplyMapper;
import com.crediya.apply.usecase.apply.ApplyUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ApplyUseCase applyUseCase;
    private final Validator validator;
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> createApply(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ApplyRequestDTO.class)
                .flatMap(dto -> {
                    log.info("📩 Request received to create apply: {}", dto.getDni());
                    // ✅ Validación con Bean Validation
                    Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        log.warn("⚠️ Validation violations in createApply: {}", violations);
                        throw new ConstraintViolationException(violations);
                    }

                    // ✅ Guardar usuario
                    return applyUseCase.saveApply(ApplyMapper.requestToDomain(dto))
                            .doOnNext(user -> log.info("✅ Apply saved: {}", user.getDni()))
                            .doOnError(err -> log.error("❌ Error saving apply {}", dto.getDni(), err))
                            .map(ApplyMapper::domainToRespons)
                            .flatMap(saved -> ServerResponse.ok().bodyValue(saved));
                });
    }
}
