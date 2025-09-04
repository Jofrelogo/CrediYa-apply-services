package com.crediya.apply.r2dbc;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.apply.gateways.ApplyRepository;
import com.crediya.apply.r2dbc.entity.ApplyEntity;
import com.crediya.apply.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ApplyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Apply,
        ApplyEntity,
        String,
        ApplyReactiveRepository
>implements ApplyRepository {

    private static final Logger log = LoggerFactory.getLogger(ApplyReactiveRepositoryAdapter.class);

    public ApplyReactiveRepositoryAdapter(ApplyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Apply.class));
    }

    @Override
    public Mono<Apply> save(Apply apply){
        log.debug("💾 Trying to save apply with DNI={}", apply.getDni());
        return super.save(apply)
                .doOnSuccess(saved -> log.info("✅ Apply persisted with DNI={}", saved.getDni()));
    }
}
