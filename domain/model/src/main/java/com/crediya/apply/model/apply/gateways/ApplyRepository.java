package com.crediya.apply.model.apply.gateways;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.common.PageQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ApplyRepository {

    Mono<Apply> save(Apply apply);

    Flux<Apply> findByStateIn(List<String> statuses, PageQuery pageQuery);
    Mono<Long> count(List<String> statuses);

    Mono<Apply> findById(UUID dni);
}
