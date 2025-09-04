package com.crediya.apply.model.apply.gateways;

import com.crediya.apply.model.apply.Apply;
import reactor.core.publisher.Mono;

public interface ApplyRepository {

    Mono<Apply> save(Apply apply);
}
