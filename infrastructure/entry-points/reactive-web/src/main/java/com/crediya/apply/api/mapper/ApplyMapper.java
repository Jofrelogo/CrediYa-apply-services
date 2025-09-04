package com.crediya.apply.api.mapper;

import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.dto.ApplyResponseDTO;
import com.crediya.apply.model.apply.Apply;

public class ApplyMapper {

    public static Apply requestToDomain(ApplyRequestDTO dto) {
        return new Apply(
                dto.getDni(),
                dto.getAmount(),
                dto.getTerm(),
                dto.getLoanType(),
                dto.getState()
        );
    }

    public static ApplyResponseDTO domainToRespons(Apply apply) {
        return new ApplyResponseDTO(
                apply.getDni(),
                apply.getAmount(),
                apply.getTerm(),
                apply.getLoanType(),
                apply.getState()
        );
    }
}
