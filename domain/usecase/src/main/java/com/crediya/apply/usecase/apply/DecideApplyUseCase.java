package com.crediya.apply.usecase.apply;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.apply.gateways.ApplyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class DecideApplyUseCase {

    private final ApplyRepository applyRepository;

    public Mono<Apply> execute(String applyId, String decision) {
        return applyRepository.findById(UUID.fromString(applyId))
                .switchIfEmpty(Mono.error(new RuntimeException("Apply not found with id: " + applyId)))
                .flatMap(existing -> {
                    existing.setState(decision);
                    System.out.println("existing = " + existing);
                    return applyRepository.save(existing);
                });
    }
}
