package com.crediya.apply.r2dbc;

import com.crediya.apply.r2dbc.entity.ApplyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ApplyReactiveRepository extends ReactiveCrudRepository<ApplyEntity, String>, ReactiveQueryByExampleExecutor<ApplyEntity> {

    // 🔎 Buscar por estados con paginación
    @Query("SELECT * FROM applys WHERE state IN (:statuses) ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    Flux<ApplyEntity> findByStateInPaged(List<String> statuses, int limit, int offset);

    // 🔎 Contar los registros por estado
    @Query("SELECT COUNT(*) FROM applys WHERE state IN (:statuses)")
    Mono<Long> count(List<String> statuses);

}
