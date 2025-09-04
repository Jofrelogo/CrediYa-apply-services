package com.crediya.apply.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Apply response DTO")
public class ApplyResponseDTO {

    @Schema(description = "Unique identifier of the user", example = "12345")
    private String dni;

    @Schema(description = "requested amount", example = "2000000")
    private double amount;

    @Schema(description = "term of the loan requested in months", example = "12")
    private int term;

    @Schema(description = "type of loan", example = "Free investment")
    private String loanType;

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
        return "Apply Response{" +
                "dni='"+ dni + '\'' +
                " amount='" + amount + '\'' +
                ", term='" + term + '\'' +
                ", state='" + state + '\'' +
                ", loanType='" + loanType + '\'' +
                '}';
    }

}
