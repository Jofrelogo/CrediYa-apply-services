package com.crediya.apply.r2dbc.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("applys")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApplyEntity {

    @Id
    private String id;
    private String dni;
    private double amount;
    private int term;
    private String state;
    private String loanType;
    private LocalDateTime createdAt;
}
