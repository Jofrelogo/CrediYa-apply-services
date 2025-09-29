-- Eliminar la columna de texto loan_type (si ya existía como string plano)
ALTER TABLE applies DROP COLUMN IF EXISTS loan_type;

-- Agregar columna con FK a loan_types
ALTER TABLE applies
ADD COLUMN loan_type_id UUID NOT NULL,
ADD CONSTRAINT fk_applies_loan_type FOREIGN KEY (loan_type_id) REFERENCES loan_types (id);

-- ===========================================
-- 3. Índices adicionales en applies
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_applies_dni ON applies (dni);
CREATE INDEX IF NOT EXISTS idx_applies_state ON applies (state);
CREATE INDEX IF NOT EXISTS idx_applies_created_at ON applies (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_applies_loan_type_id ON applies (loan_type_id);