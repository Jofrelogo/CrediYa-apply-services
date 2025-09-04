package com.crediya.apply.api.exception;

import com.crediya.apply.model.common.DomainException;
import com.crediya.apply.model.common.ErrorCode;
import com.crediya.apply.model.common.ErrorDetail;
import com.crediya.apply.model.common.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * Manejo de excepciones del dominio
     */
    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDomainException(DomainException ex) {
        log.warn("⚠️ Domain exception capturada: {} - {}", ex.getErrorCode(), ex.getMessage());
        ErrorDetail detail = new ErrorDetail(ex.getErrorCode().name(), ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                mapToStatus(ex.getErrorCode()).value(),
                ex.getMessage(),
                List.of(detail)
        );

        return Mono.just(ResponseEntity
                .status(mapToStatus(ex.getErrorCode()))
                .body(response));
    }

    /**
     * Manejo de errores de validación con @Valid / Validator
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(ConstraintViolationException ex) {
        List<ErrorDetail> errors = ex.getConstraintViolations().stream()
                .map(v -> new ErrorDetail(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );

        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    /**
     * Manejo de cualquier otra excepción no controlada
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        log.error("❌ Error inesperado capturado en GlobalExceptionHandler", ex);
        ErrorDetail detail = new ErrorDetail(ErrorCode.GENERIC_ERROR.name(), ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error",
                List.of(detail)
        );

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }

    /**
     * Mapear ErrorCode a HttpStatus
     */
    private HttpStatus mapToStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case EMAIL_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}





