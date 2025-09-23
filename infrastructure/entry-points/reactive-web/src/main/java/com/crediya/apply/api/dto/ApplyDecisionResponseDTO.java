package com.crediya.apply.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = " Decision Apply response DTO")
public class ApplyDecisionResponseDTO {
    String applyId;
    String status;
    String comment;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyDni) {
        this.applyId = applyDni;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

