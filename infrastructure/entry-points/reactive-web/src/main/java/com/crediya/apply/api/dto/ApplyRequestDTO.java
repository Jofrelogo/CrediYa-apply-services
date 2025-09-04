package com.crediya.apply.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(name = "ApplyRequestDTO", description = "Data transfer object for creating a new apply")
public class ApplyRequestDTO {

    @NotBlank(message = "Dni is required")
    @Schema(description = "Unique identifier of the user", example = "12345")
    private String dni;

    @NotNull(message = "Amount is required")
    @Positive(message = "The amount must be greater than 0")
    @Schema(description = "requested amount", example = "2000000")
    private double amount;

    @NotNull(message = "Term is required")
    @Positive(message = "The term must be greater than 0")
    @Schema(description = "term of the loan requested in months", example = "12")
    private int term;

    @NotBlank(message = "LoanType is required")
    @Schema(description = "type of loan", example = "Free investment")
    private String loanType;

    @NotBlank(message = "State  is required")
    @Schema(description = "application status", example = "Earring")
    private String state;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Apply Request{" +
                "dni='"+ dni + '\'' +
                " amount='" + amount + '\'' +
                ", term='" + term + '\'' +
                ", state='" + state + '\'' +
                ", loanType='" + loanType + '\'' +
                '}';
    }
}
