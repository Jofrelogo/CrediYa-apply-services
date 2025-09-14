package com.crediya.apply.api;

import com.crediya.apply.api.config.ApplyPath;
import com.crediya.apply.model.common.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RouterRestTest {

    private Handler handler;
    private ApplyPath applyPath;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // Mock Handler
        handler = mock(Handler.class);

        // Mock responses for handler methods
        when(handler.createApply(any()))
                .thenReturn(ServerResponse.ok().bodyValue("created"));
        when(handler.listForReview(any()))
                .thenReturn(ServerResponse.ok().bodyValue(
                        new PageResponse<>(List.of("apply1", "apply2"), 0, 10, 2)
                ));

        // Fake ApplyPath
        applyPath = mock(ApplyPath.class);
        when(applyPath.getSaveApply()).thenReturn("/api/v1/apply");
        when(applyPath.getListApplys()).thenReturn("/api/v1/apply");

        // Build router function
        RouterRest routerRest = new RouterRest(applyPath);
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testPostRoute() {
        webTestClient.post()
                .uri("/api/v1/apply")
                .body(BodyInserters.fromValue("{\"dni\":\"123\"}"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("created");

        verify(handler, times(1)).createApply(any());
    }

    @Test
    void testGetRoute() {
        webTestClient.get()
                .uri("/api/v1/apply?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<String>>() {})
                .consumeWith(response -> {
                    PageResponse<String> page = response.getResponseBody();
                    assertNotNull(page);
                    assertEquals(0, page.getPage());
                    assertEquals(10, page.getSize());
                    assertEquals(2, page.getTotalElements());
                });

        verify(handler, times(1)).listForReview(any());
    }
}

