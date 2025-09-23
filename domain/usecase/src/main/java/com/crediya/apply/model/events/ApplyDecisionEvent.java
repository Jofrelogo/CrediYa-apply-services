package com.crediya.apply.model.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyDecisionEvent {
    private String applyId;
    private String applicantDni;
    private String decision; // APPROVED | REJECTED
    private double amount;
    private int term;
}
