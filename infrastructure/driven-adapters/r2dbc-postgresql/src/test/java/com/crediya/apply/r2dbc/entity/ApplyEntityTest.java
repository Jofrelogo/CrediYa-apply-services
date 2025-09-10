package com.crediya.apply.r2dbc.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApplyEntityTest {

    @Test
    void testGettersAndSetters() {
        ApplyEntity entity = new ApplyEntity();

        String id = "123";
        String dni = "987654321";
        double amount = 2000000.0;
        int term = 12;
        String state = "PENDING_REVIEW";
        String loanType = "Free investment";
        LocalDateTime createdAt = LocalDateTime.now();

        entity.setId(id);
        entity.setDni(dni);
        entity.setAmount(amount);
        entity.setTerm(term);
        entity.setState(state);
        entity.setLoanType(loanType);
        entity.setCreatedAt(createdAt);

        assertEquals(id, entity.getId());
        assertEquals(dni, entity.getDni());
        assertEquals(amount, entity.getAmount());
        assertEquals(term, entity.getTerm());
        assertEquals(state, entity.getState());
        assertEquals(loanType, entity.getLoanType());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void testDefaultValues() {
        ApplyEntity entity = new ApplyEntity();
        assertNull(entity.getId());
        assertNull(entity.getDni());
        assertEquals(0.0, entity.getAmount());
        assertEquals(0, entity.getTerm());
        assertNull(entity.getState());
        assertNull(entity.getLoanType());
        assertNull(entity.getCreatedAt());
    }
}

