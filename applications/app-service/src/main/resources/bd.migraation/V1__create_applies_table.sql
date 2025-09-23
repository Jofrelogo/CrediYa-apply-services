-- Habilitar extensión de UUID en Postgres (si no existe)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla applies con UUID como PK
CREATE TABLE IF NOT EXISTS applies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- PK con UUID
    dni VARCHAR(20) NOT NULL,                        -- Documento del solicitante
    amount NUMERIC(12,2) NOT NULL,                   -- Monto solicitado
    term INT NOT NULL,                               -- Plazo (meses)
    loan_type VARCHAR(50) NOT NULL,                  -- Tipo de préstamo
    state VARCHAR(50) NOT NULL,                      -- Estado (ej: PENDING, APPROVED, REJECTED)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL -- Fecha de creación
);

-- Índices recomendados para optimizar búsquedas
CREATE INDEX IF NOT EXISTS idx_applies_dni ON applies (dni);
CREATE INDEX IF NOT EXISTS idx_applies_state ON applies (state);
CREATE INDEX IF NOT EXISTS idx_applies_created_at ON applies (created_at DESC);