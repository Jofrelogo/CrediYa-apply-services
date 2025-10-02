package com.crediya.apply.sqs.listener.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "adapter.sqs")
public class AdapterSqsProperties {
    private String region;
    private String resultQueue;
    private String endpoint; // opcional, solo para LocalStack
}

