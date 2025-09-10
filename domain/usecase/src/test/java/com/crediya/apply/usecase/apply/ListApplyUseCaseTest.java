package com.crediya.apply.usecase.apply;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.apply.gateways.ApplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplyUseCaseTest {

    @Mock
    private ApplyRepository applyRepository;

    @InjectMocks
    private ApplyUseCase applyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveApply_shouldReturnSavedApply() {
        // Arrange
        Apply apply = new Apply();
        apply.setDni("123");
        apply.setState("PENDING_REVIEW");
        apply.setTerm(11);
        apply.setAmount(200000);
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.just(apply));

        // Act
        Mono<Apply> result = applyUseCase.saveApply(apply);

        // Assert
        StepVerifier.create(result)
                .expectNext(apply)
                .verifyComplete();

        verify(applyRepository, times(1)).save(apply);
    }

    @Test
    void saveApply_shouldPropagateError() {
        // Arrange
        Apply apply = new Apply();
        apply.setDni("123");
        apply.setState("PENDING_REVIEW");
        apply.setTerm(11);
        apply.setAmount(200000);
        RuntimeException exception = new RuntimeException("DB error");
        when(applyRepository.save(any(Apply.class))).thenReturn(Mono.error(exception));

        // Act
        Mono<Apply> result = applyUseCase.saveApply(apply);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("DB error"))
                .verify();

        verify(applyRepository, times(1)).save(apply);
    }
}


