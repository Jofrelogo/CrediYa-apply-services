package com.crediya.apply.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private Instant timestamp; // Momento del error
    private int status;              // Código HTTP
    private String error;
    private List<ErrorDetail> errors; // Lista de errores

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }
}


