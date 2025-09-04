package com.crediya.apply.usecase.apply;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.apply.gateways.ApplyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplyUseCase {

    private final ApplyRepository applyRepository;

    public Mono<Apply> saveApply(Apply apply) { return  applyRepository.save(apply); }

}
