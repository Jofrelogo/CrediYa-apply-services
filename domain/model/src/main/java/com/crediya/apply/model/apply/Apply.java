package com.crediya.apply.model.apply;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Apply {

    private String dni;
    private double amount;
    private int term;
    private String loanType;
    private String state;

    @Override
    public String toString() {
        return "Apply{" +
                "dni='"+ dni + '\'' +
                " amount='" + amount + '\'' +
                ", term='" + term + '\'' +
                ", state='" + state + '\'' +
                ", loanType='" + loanType + '\'' +
                '}';
    }

}
