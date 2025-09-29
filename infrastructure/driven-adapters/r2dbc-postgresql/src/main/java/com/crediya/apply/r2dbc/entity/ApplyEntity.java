package com.crediya.apply.r2dbc.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("applies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApplyEntity {

    @Id
    private UUID id;
    private String dni;
    private double amount;
    private int term;
    private String state;
    private UUID loanTypeId;

    @Column("created_at")
    @ReadOnlyProperty
    private LocalDateTime createdAt;
}
