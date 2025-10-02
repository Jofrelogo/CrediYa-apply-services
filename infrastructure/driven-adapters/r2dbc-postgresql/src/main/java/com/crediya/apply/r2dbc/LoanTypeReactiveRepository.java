package com.crediya.apply.r2dbc;

import com.crediya.apply.r2dbc.entity.ApplyEntity;
import com.crediya.apply.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, UUID>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {
}
