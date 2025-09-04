package com.crediya.apply.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDetail {
    private String code;     // Código de error (ej: USR_404_001)
    private String message;  // Mensaje legible para el cliente
}


