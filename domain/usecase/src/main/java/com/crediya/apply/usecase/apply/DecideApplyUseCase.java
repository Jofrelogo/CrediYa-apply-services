package com.crediya.apply.usecase.apply;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.apply.gateways.ApplyRepository;
import com.crediya.apply.model.events.ApplyDecisionEvent;
import com.crediya.apply.model.events.gateways.MessagePublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class DecideApplyUseCase {

    private final ApplyRepository applyRepository;
    private final MessagePublisher messagePublisher;

    public Mono<Apply> decideApply(String applyId, String decision) {
        return applyRepository.findById(UUID.fromString(applyId))
                .switchIfEmpty(Mono.error(new RuntimeException("Apply not found with id: " + applyId)))
                .flatMap(existing -> {
                    existing.setState(decision.toUpperCase());

                    return applyRepository.save(existing)
                            .flatMap(saved -> {
                                ApplyDecisionEvent event = ApplyDecisionEvent.builder()
                                        .applyId(saved.getId().toString())
                                        .applicantDni(saved.getDni())
                                        .decision(saved.getState())
                                        .amount(saved.getAmount())
                                        .term(saved.getTerm())
                                        .build();

                                // Publicas el objeto, no un String
                                return messagePublisher.publishDecide(event)
                                        .thenReturn(Apply.builder()
                                                .id(saved.getId())
                                                .state(saved.getState())
                                                .dni(saved.getDni())
                                                .amount(saved.getAmount())
                                                .term(saved.getTerm())
                                                //.comment(saved.getComment())
                                                .build());
                            });
                });
    }
}
