package com.crediya.apply.model.apply.gateways;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.common.PageQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApplyRepository {

    Mono<Apply> save(Apply apply);

    Flux<Apply> findByStateIn(List<String> statuses, PageQuery pageQuery);
    Mono<Long> count(List<String> statuses);
}
