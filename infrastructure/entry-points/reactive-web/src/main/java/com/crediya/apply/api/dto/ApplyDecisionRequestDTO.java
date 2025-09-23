package com.crediya.apply.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "ApplyDecisionRequestDTO", description = "Data transfer object for update a apply")
public class ApplyDecisionRequestDTO {

    @NotBlank
    String applyId;

    @NotBlank
    @Pattern(regexp = "APPROVED|REJECTED")
    String decision;

    String comment;

    public String getApplyId() {
        return applyId;
    }

    public String getDecision() {
        return decision;
    }

    public String getComment() {
        return comment;
    }
}
