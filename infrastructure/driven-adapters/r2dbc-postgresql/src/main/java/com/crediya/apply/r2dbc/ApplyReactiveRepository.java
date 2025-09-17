package com.crediya.apply.r2dbc;

import com.crediya.apply.r2dbc.entity.ApplyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;


public interface ApplyReactiveRepository extends ReactiveCrudRepository<ApplyEntity, String>, ReactiveQueryByExampleExecutor<ApplyEntity> {

    // 🔎 Buscar por estados con paginación
    @Query("SELECT * FROM applies WHERE state IN (:statuses) ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    Flux<ApplyEntity> findByStateInPaged(List<String> statuses, int limit, int offset);

    // 🔎 Contar los registros por estado
    @Query("SELECT COUNT(*) FROM applies WHERE state IN (:statuses)")
    Mono<Long> count(List<String> statuses);

    @Query("SELECT * FROM applies WHERE id = :id LIMIT 1")
    Mono<ApplyEntity> findByIdSafe(UUID id);
}
