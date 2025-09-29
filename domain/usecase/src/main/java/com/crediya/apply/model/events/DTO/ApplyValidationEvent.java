package com.crediya.apply.model.events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyValidationEvent {
    private UUID applyId;
    private String dni;
    private String email;
    private Double baseSalary;
    private Double amount;
    private Integer term;
    private UUID loanTypeId;

    @Override
    public String toString() {
        return "ApplyValidationEvent{" +
                "applyId='"+ applyId + '\'' +
                " dni='"+ dni + '\'' +
                " email='"+ email + '\'' +
                " amount='"+ amount + '\'' +
                " baseSalary='" + baseSalary + '\'' +
                ", term='" + term + '\'' +
                ", loanType='" + loanTypeId + '\'' +
                '}';
    }
}

