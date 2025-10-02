package com.crediya.apply.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("loan_types")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoanTypeEntity {

    @Id
    private UUID id;
    private String name;
    private String description;

    @Column("automatic_validation")
    private boolean isAutomaticValidation;
}
