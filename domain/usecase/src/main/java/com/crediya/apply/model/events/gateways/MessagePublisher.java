package com.crediya.apply.model.events.gateways;

import reactor.core.publisher.Mono;

public interface MessagePublisher {
    Mono<Void> publishDecide(Object event);

    Mono<Void> publishValidation(Object event);
}
