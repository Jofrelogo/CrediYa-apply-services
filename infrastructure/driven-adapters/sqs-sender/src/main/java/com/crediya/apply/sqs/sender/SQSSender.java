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
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> publish(Object event) {
        return Mono.fromCallable(() -> {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(event);

                    log.info("JSON enviado a SQS: {}", json);

                    return buildRequest(json);
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent with ID: {}", response.messageId()))
                .then();
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }
}
