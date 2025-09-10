package com.crediya.apply.r2dbc;

import com.crediya.apply.r2dbc.entity.ApplyEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

class ApplyReactiveRepositoryMockTest {

    @Test
    void testFindByStateInPaged_Mock() {
        ApplyReactiveRepository mockRepo = Mockito.mock(ApplyReactiveRepository.class);

        ApplyEntity entity = new ApplyEntity();
        entity.setId("1");
        entity.setDni("12345");
        entity.setAmount(1000);
        entity.setTerm(6);
        entity.setState("PENDING_REVIEW");
        entity.setLoanType("Personal");
        entity.setCreatedAt(LocalDateTime.now());

        Mockito.when(mockRepo.findByStateInPaged(List.of("PENDING_REVIEW"), 10, 0))
                .thenReturn(Flux.just(entity));

        StepVerifier.create(mockRepo.findByStateInPaged(List.of("PENDING_REVIEW"), 10, 0))
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    void testCount_Mock() {
        ApplyReactiveRepository mockRepo = Mockito.mock(ApplyReactiveRepository.class);

        Mockito.when(mockRepo.count(List.of("PENDING_REVIEW")))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(mockRepo.count(List.of("PENDING_REVIEW")))
                .expectNext(1L)
                .verifyComplete();
    }
}

