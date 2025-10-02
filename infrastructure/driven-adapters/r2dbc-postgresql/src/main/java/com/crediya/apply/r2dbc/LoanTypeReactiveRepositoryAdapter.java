package com.crediya.apply.r2dbc;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.loantype.LoanType;
import com.crediya.apply.model.loantype.gateways.LoanTypeRepository;
import com.crediya.apply.r2dbc.entity.LoanTypeEntity;
import com.crediya.apply.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        UUID,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {

    private static final Logger log = LoggerFactory.getLogger(ApplyReactiveRepositoryAdapter.class);

    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }

    @Override
    public Mono<LoanType> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, LoanType.class));
    }

}