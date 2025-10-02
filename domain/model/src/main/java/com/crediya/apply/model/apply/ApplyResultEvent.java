package com.crediya.apply.model.apply;

import lombok.Data;

import java.util.UUID;

@Data
public class ApplyResultEvent {
    private UUID applyId;
    private String dni;
    private String email;
    private Double cuotaNueva;          // Cuota calculada
    private Double capacidadMax;        // Capacidad máxima
    private Double capacidadDisponible; // Capacidad disponible después de validar
    private String resultado;
}
