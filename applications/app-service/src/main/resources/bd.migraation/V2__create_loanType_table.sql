-- Habilitar extensión de UUID en Postgres (si no existe)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===========================================
-- 1. Crear tabla loan_types
-- ===========================================
CREATE TABLE IF NOT EXISTS loan_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),    -- PK con UUID
    name VARCHAR(50) UNIQUE NOT NULL,                  -- Nombre del tipo de préstamo (ej: free, personal, hipotecario)
    description TEXT,                                  -- Descripción opcional
    automatic_validation BOOLEAN DEFAULT FALSE NOT NULL, -- Indica si va a validación automática
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Índice para búsquedas rápidas por nombre
CREATE INDEX IF NOT EXISTS idx_loan_types_name ON loan_types (name);