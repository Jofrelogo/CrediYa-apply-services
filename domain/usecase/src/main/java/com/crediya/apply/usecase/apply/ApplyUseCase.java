package com.crediya.apply.usecase.apply;

import com.crediya.apply.model.apply.Apply;

import com.crediya.apply.model.apply.gateways.ApplyRepository;
import com.crediya.apply.model.events.DTO.ApplyValidationEvent;
import com.crediya.apply.model.events.gateways.MessagePublisher;
import com.crediya.apply.model.jwt.JwtProvider;
import com.crediya.apply.model.loantype.gateways.LoanTypeRepository;
import com.crediya.apply.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApplyUseCase {

    private final ApplyRepository applyRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final MessagePublisher messagePublisher;
    private final UserRepository userRepository;

    public Mono<Apply> saveApply(Apply apply, String token) {

        return applyRepository.save(apply) // devuelve Mono<Apply>
                .flatMap(saved ->
                        loanTypeRepository.findById(saved.getLoanTypeId()) // Mono<LoanType>
                                .flatMap(loanType -> {
                                    if (loanType.isAutomaticValidation()) {
                                        return userRepository.getUserByEmail(token)
                                                .flatMap(user -> {
                                                    ApplyValidationEvent event = ApplyValidationEvent.builder()
                                                            .applyId(saved.getId())
                                                            .dni(user.getDni())
                                                            .email(user.getEmail())
                                                            .baseSalary(user.getBaseSalary())
                                                            .amount(saved.getAmount())
                                                            .term(saved.getTerm())
                                                            .loanTypeId(saved.getLoanTypeId())
                                                            .build();

                                                    System.out.println("event = " + event);

                                                // Enviar mensaje a SQS de forma reactiva
                                                return messagePublisher.publishValidation(event)
                                                        .then(Mono.just(saved.withState("PENDING_VALIDATION")));

                                                });


                                    } else {
                                        return Mono.just(saved.withState("PENDING_MANUAL_REVIEW"));
                                    }
                                })
                )
                // Guardamos el estado actualizado de la solicitud
                .flatMap(applyRepository::save);
    }

}
