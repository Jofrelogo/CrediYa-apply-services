package com.crediya.apply.model.loantype.gateways;

import com.crediya.apply.model.loantype.LoanType;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface LoanTypeRepository {

    Mono<LoanType> findById(UUID id);
}
