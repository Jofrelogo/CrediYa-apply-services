package com.crediya.apply.r2dbc;


import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.common.PageQuery;
import com.crediya.apply.r2dbc.entity.ApplyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

class ApplyReactiveRepositoryAdapterTest {

    private ApplyReactiveRepository mockRepo;
    private ObjectMapper mapper;
    private ApplyReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        mockRepo = Mockito.mock(ApplyReactiveRepository.class);
        mapper = Mockito.mock(ObjectMapper.class);

        // 🔹 Mapeo de ApplyEntity -> Apply
        Mockito.when(mapper.map(Mockito.any(ApplyEntity.class), eq(Apply.class)))
                .thenAnswer(invocation -> {
                    ApplyEntity e = invocation.getArgument(0);
                    return new Apply(
                            e.getDni(),
                            e.getAmount(),
                            e.getTerm(),
                            e.getLoanType(),
                            e.getState()
                    );
                });

        // 🔹 Mapeo de Apply -> ApplyEntity
        Mockito.when(mapper.map(Mockito.any(Apply.class), eq(ApplyEntity.class)))
                .thenAnswer(invocation -> {
                    Apply a = invocation.getArgument(0);
                    ApplyEntity e = new ApplyEntity();
                    e.setId("1");
                    e.setDni(a.getDni());
                    e.setAmount(a.getAmount());
                    e.setTerm(a.getTerm());
                    e.setLoanType(a.getLoanType());
                    e.setState(a.getState());
                    e.setCreatedAt(LocalDateTime.now());
                    return e;
                });

        adapter = new ApplyReactiveRepositoryAdapter(mockRepo, mapper);
    }

    @Test
    void testSave() {
        Apply apply = new Apply("123", 2000, 12, "Free investment", "PENDING");
        ApplyEntity entity = new ApplyEntity();
        entity.setId("1");
        entity.setDni("123");
        entity.setAmount(2000);
        entity.setTerm(12);
        entity.setLoanType("Free investment");
        entity.setState("PENDING");
        entity.setCreatedAt(LocalDateTime.now());

        Mockito.when(mockRepo.save(any(ApplyEntity.class))).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.save(apply))
                .expectNextMatches(saved -> saved.getDni().equals("123") && saved.getAmount() == 2000)
                .verifyComplete();
    }

    @Test
    void testFindByStateIn() {
        PageQuery pq = new PageQuery(0, 10);
        ApplyEntity entity = new ApplyEntity();
        entity.setId("1");
        entity.setDni("999");
        entity.setAmount(5000);
        entity.setTerm(24);
        entity.setLoanType("Car loan");
        entity.setState("PENDING");
        entity.setCreatedAt(LocalDateTime.now());

        Mockito.when(mockRepo.findByStateInPaged(anyList(), eq(10), eq(0)))
                .thenReturn(Flux.just(entity));

        StepVerifier.create(adapter.findByStateIn(List.of("PENDING"), pq))
                .expectNextMatches(apply -> apply.getDni().equals("999") && apply.getLoanType().equals("Car loan"))
                .verifyComplete();
    }

    @Test
    void testCount() {
        Mockito.when(mockRepo.count(anyList())).thenReturn(Mono.just(5L));

        StepVerifier.create(adapter.count(List.of("PENDING")))
                .expectNext(5L)
                .verifyComplete();
    }
}



