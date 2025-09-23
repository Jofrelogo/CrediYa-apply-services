package com.crediya.apply.model.events.gateways;

import reactor.core.publisher.Mono;

public interface MessagePublisher {
    Mono<Void> publish(Object event);
}
