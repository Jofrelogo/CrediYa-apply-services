package com.crediya.apply.sqs.listener.config;

import com.crediya.apply.sqs.listener.properties.AdapterSqsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import java.net.URI;
import java.time.Duration;


@Configuration
public class SQSConfig {

    @Bean
    public SqsAsyncClient configSqs(AdapterSqsProperties properties) {
        return SqsAsyncClient.builder()
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .httpClientBuilder(NettyNioAsyncHttpClient.builder()
                        .maxConcurrency(50) // conexiones concurrentes
                        .connectionTimeout(Duration.ofSeconds(10))
                        .readTimeout(Duration.ofSeconds(30)))
                .build();
    }
}
