package com.crediya.apply.model.apply;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class Apply {

    private UUID id;
    private String dni;
    private double amount;
    private int term;
    private UUID loanTypeId;
    private String state;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Apply{" +
                "dni='"+ dni + '\'' +
                " amount='" + amount + '\'' +
                ", term='" + term + '\'' +
                ", state='" + state + '\'' +
                ", loanType='" + loanTypeId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

}
