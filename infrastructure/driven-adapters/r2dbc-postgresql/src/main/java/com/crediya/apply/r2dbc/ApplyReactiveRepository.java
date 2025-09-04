package com.crediya.apply.r2dbc;

import com.crediya.apply.r2dbc.entity.ApplyEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface ApplyReactiveRepository extends ReactiveCrudRepository<ApplyEntity, String>, ReactiveQueryByExampleExecutor<ApplyEntity> {

}
