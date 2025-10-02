package com.crediya.apply.sqs.listener;

import com.crediya.apply.model.apply.ApplyResultEvent;
import com.crediya.apply.usecase.apply.ApplyUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class SQSProcessor  {

    private final ApplyUseCase applyUseCase;

    @SqsListener("${adapter.sqs.resultQueue}")
    public void handleApplyResult(@Payload ApplyResultEvent event) {
        log.info("📩 Mensaje recibido de la cola: {}", event);

        Mono<Void> update = applyUseCase.updateStateFromResult(event);

        // Importante suscribirse para que se ejecute en programación reactiva
        update.subscribe(
                ok -> log.info("✅ Estado de apply {} actualizado", event.getApplyId()),
                error -> log.error("❌ Error actualizando apply {}", event.getApplyId(), error)
        );
    }

}
