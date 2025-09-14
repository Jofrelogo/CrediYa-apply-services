package com.crediya.apply.api.mapper;

import org.junit.jupiter.api.Test;
import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.dto.ApplyResponseDTO;
import com.crediya.apply.model.apply.Apply;
import static org.junit.jupiter.api.Assertions.*;

class ApplyMapperTest {

    @Test
    void testRequestToDomain() {
        // given
        ApplyRequestDTO dto = new ApplyRequestDTO();
        dto.setDni("123456");
        dto.setAmount(10000.0);
        dto.setTerm(12);
        dto.setLoanType("PERSONAL");
        dto.setState("PENDING_REVIEW");

        // when
        Apply domain = ApplyMapper.requestToDomain(dto);

        // then
        assertNotNull(domain);
        assertEquals("123456", domain.getDni());
        assertEquals(10000.0, domain.getAmount());
        assertEquals(12, domain.getTerm());
        assertEquals("PERSONAL", domain.getLoanType());
        assertEquals("PENDING_REVIEW", domain.getState());
    }

    @Test
    void testDomainToResponse() {
        // given
        Apply domain = new Apply(
                "654321",
                20000.0,
                24,
                "VEHICLE",
                "APPROVED"
        );

        // when
        ApplyResponseDTO response = ApplyMapper.domainToRespons(domain);

        // then
        assertNotNull(response);
        assertEquals("654321", response.getDni());
        assertEquals(20000.0, response.getAmount());
        assertEquals(24, response.getTerm());
        assertEquals("VEHICLE", response.getLoanType());
        assertEquals("APPROVED", response.getState());
    }
}
