package com.crediya.apply.model.loantype;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {

    private UUID id;
    private String name;
    private String description;
    private boolean isAutomaticValidation;

    @Override
    public String toString() {
        return "loan Type{" +
                "id='"+ id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isAutomaticValidation='" + isAutomaticValidation + '\'' +
                '}';
    }
}
