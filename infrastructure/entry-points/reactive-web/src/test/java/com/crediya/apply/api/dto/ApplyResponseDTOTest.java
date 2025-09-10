package com.crediya.apply.api.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplyResponseDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        ApplyResponseDTO dto = new ApplyResponseDTO("12345", 2000000, 12, "Free investment", "PENDING_REVIEW");

        assertEquals("12345", dto.getDni());
        assertEquals(2000000, dto.getAmount());
        assertEquals(12, dto.getTerm());
        assertEquals("Free investment", dto.getLoanType());
        assertEquals("PENDING_REVIEW", dto.getState());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ApplyResponseDTO dto = new ApplyResponseDTO();
        dto.setDni("67890");
        dto.setAmount(1500000);
        dto.setTerm(24);
        dto.setLoanType("Mortgage");
        dto.setState("APPROVED");

        assertEquals("67890", dto.getDni());
        assertEquals(1500000, dto.getAmount());
        assertEquals(24, dto.getTerm());
        assertEquals("Mortgage", dto.getLoanType());
        assertEquals("APPROVED", dto.getState());
    }

    @Test
    void testToStringContainsValues() {
        ApplyResponseDTO dto = new ApplyResponseDTO("12345", 2000000, 12, "Free investment", "PENDING_REVIEW");

        String toString = dto.toString();
        assertTrue(toString.contains("12345"));
        assertTrue(toString.contains("2000000"));
        assertTrue(toString.contains("12"));
        assertTrue(toString.contains("Free investment"));
        assertTrue(toString.contains("PENDING_REVIEW"));
    }
}
