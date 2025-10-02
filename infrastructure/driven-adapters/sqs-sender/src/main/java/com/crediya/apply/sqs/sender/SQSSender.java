package com.crediya.apply.sqs.sender;

import com.crediya.apply.model.events.gateways.MessagePublisher;
import com.crediya.apply.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements MessagePublisher {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private ObjectMapper mapper ;

    @Override
    public Mono<Void> publishDecide(Object event) {
        return Mono.fromCallable(() -> {
                    mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(event);

                    log.info("JSON enviado a SQS ({}): {}",QueueType.NOTIFICATION, json);

                    return buildRequest(json, QueueType.NOTIFICATION);
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent with ID: {}", response.messageId()))
                .then();
    }

    @Override
    public Mono<Void> publishValidation(Object event) {
        return Mono.fromCallable(() -> {
                    mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(event);

                    log.info("JSON enviado a SQS ({}): {}", QueueType.CAPACITY_VALIDATION, json);
                    return buildRequest(json, QueueType.CAPACITY_VALIDATION);
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent with ID: {}", response.messageId()))
                .then();
    }

    private SendMessageRequest buildRequest(String message, QueueType type) {
        String queueUrl = switch (type) {
            case NOTIFICATION -> properties.decideApplyQueueUrl();
            case CAPACITY_VALIDATION -> properties.capacityValidationQueueUrl();
        };
        System.out.println("queueUrl = " + queueUrl );
        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
    }

    public enum QueueType {
        NOTIFICATION,
        CAPACITY_VALIDATION
    }
}
