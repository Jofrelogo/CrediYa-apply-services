package com.crediya.apply.model.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    void constructor_shouldCalculateTotalPagesCorrectly() {
        List<String> content = Arrays.asList("A", "B", "C");
        PageResponse<String> response = new PageResponse<>(content, 1, 3, 8);

        assertThat(response.getContent()).containsExactly("A", "B", "C");
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(3);
        assertThat(response.getTotalElements()).isEqualTo(8);
        assertThat(response.getTotalPages()).isEqualTo(3); // 8 / 3 = 2.67 → ceil = 3
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        PageResponse<Integer> response = new PageResponse<>(Collections.emptyList(), 0, 1, 1);

        response.setContent(Arrays.asList(10, 20));
        response.setPage(2);
        response.setSize(5);
        response.setTotalElements(15);
        response.setTotalPages(4);

        assertThat(response.getContent()).containsExactly(10, 20);
        assertThat(response.getPage()).isEqualTo(2);
        assertThat(response.getSize()).isEqualTo(5);
        assertThat(response.getTotalElements()).isEqualTo(15);
        assertThat(response.getTotalPages()).isEqualTo(4);
    }

    @Test
    void constructor_shouldHandleExactDivisionWithoutRounding() {
        PageResponse<String> response = new PageResponse<>(Collections.emptyList(), 0, 4, 8);
        assertThat(response.getTotalPages()).isEqualTo(2); // exact division, no rounding up
    }

    @Test
    void constructor_shouldHandleSingleElement() {
        PageResponse<String> response = new PageResponse<>(Collections.singletonList("X"), 0, 1, 1);

        assertThat(response.getContent()).containsExactly("X");
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }
}
