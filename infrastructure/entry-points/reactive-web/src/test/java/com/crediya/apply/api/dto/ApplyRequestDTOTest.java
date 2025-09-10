package com.crediya.apply.api.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ApplyRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidApplyRequest_thenNoViolations() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("12345");
        dto.setAmount(2000000);
        dto.setTerm(12);
        dto.setLoanType("Free investment");
        dto.setState("PENDING_REVIEW");

        Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Should not have any violations for valid DTO");
    }

    @Test
    void whenDniIsBlank_thenValidationFails() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("   "); // inválido
        dto.setAmount(2000000);
        dto.setTerm(12);
        dto.setLoanType("Free investment");
        dto.setState("PENDING_REVIEW");

        Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Dni is required")));
    }

    @Test
    void whenAmountIsZeroOrNegative_thenValidationFails() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("12345");
        dto.setAmount(0); // inválido
        dto.setTerm(12);
        dto.setLoanType("Free investment");
        dto.setState("PENDING_REVIEW");

        Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The amount must be greater than 0")));
    }

    @Test
    void whenTermIsZeroOrNegative_thenValidationFails() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("12345");
        dto.setAmount(2000000);
        dto.setTerm(0); // inválido
        dto.setLoanType("Free investment");
        dto.setState("PENDING_REVIEW");

        Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The term must be greater than 0")));
    }

    @Test
    void whenLoanTypeIsBlank_thenValidationFails() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("12345");
        dto.setAmount(2000000);
        dto.setTerm(12);
        dto.setLoanType("   "); // inválido
        dto.setState("PENDING_REVIEW");

        Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("LoanType is required")));
    }

    @Test
    void whenStateIsBlank_thenValidationFails() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("12345");
        dto.setAmount(2000000);
        dto.setTerm(12);
        dto.setLoanType("Free investment");
        dto.setState("   "); // inválido

        Set<ConstraintViolation<ApplyRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("State  is required")));
    }

    @Test
    void testToStringContainsValues() {
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("12345");
        dto.setAmount(2000000);
        dto.setTerm(12);
        dto.setLoanType("Free investment");
        dto.setState("PENDING_REVIEW");

        String toString = dto.toString();
        assertTrue(toString.contains("12345"));
        assertTrue(toString.contains("2000000"));
        assertTrue(toString.contains("12"));
        assertTrue(toString.contains("Free investment"));
        assertTrue(toString.contains("PENDING_REVIEW"));
    }
}
