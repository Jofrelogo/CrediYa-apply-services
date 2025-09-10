package com.crediya.apply.model.apply;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplyTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Apply apply = new Apply();
        apply.setDni("12345");
        apply.setAmount(2000.0);
        apply.setTerm(12);
        apply.setLoanType("Free Investment");
        apply.setState("PENDING");

        assertEquals("12345", apply.getDni());
        assertEquals(2000.0, apply.getAmount());
        assertEquals(12, apply.getTerm());
        assertEquals("Free Investment", apply.getLoanType());
        assertEquals("PENDING", apply.getState());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        Apply apply = new Apply("67890", 5000.0, 24, "Housing", "APPROVED");

        assertEquals("67890", apply.getDni());
        assertEquals(5000.0, apply.getAmount());
        assertEquals(24, apply.getTerm());
        assertEquals("Housing", apply.getLoanType());
        assertEquals("APPROVED", apply.getState());
    }

    @Test
    void testBuilder() {
        Apply apply = Apply.builder()
                .dni("11111")
                .amount(1500.0)
                .term(6)
                .loanType("Education")
                .state("REJECTED")
                .build();

        assertEquals("11111", apply.getDni());
        assertEquals(1500.0, apply.getAmount());
        assertEquals(6, apply.getTerm());
        assertEquals("Education", apply.getLoanType());
        assertEquals("REJECTED", apply.getState());
    }

    @Test
    void testToBuilder() {
        Apply apply = Apply.builder()
                .dni("22222")
                .amount(3000.0)
                .term(18)
                .loanType("Car Loan")
                .state("PENDING")
                .build();

        Apply modified = apply.toBuilder()
                .amount(4000.0)
                .state("APPROVED")
                .build();

        assertEquals("22222", modified.getDni());
        assertEquals(4000.0, modified.getAmount());
        assertEquals(18, modified.getTerm());
        assertEquals("Car Loan", modified.getLoanType());
        assertEquals("APPROVED", modified.getState());
    }

    @Test
    void testToString() {
        Apply apply = new Apply("33333", 1000.0, 3, "Personal", "PENDING");

        String result = apply.toString();

        assertTrue(result.contains("33333"));
        assertTrue(result.contains("1000.0"));
        assertTrue(result.contains("3"));
        assertTrue(result.contains("Personal"));
        assertTrue(result.contains("PENDING"));
    }
}
