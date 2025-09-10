package com.crediya.apply.model.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageQueryTest {

    @Test
    void testConstructorAndGetters() {
        PageQuery query = new PageQuery(2, 10);

        assertEquals(2, query.getPage());
        assertEquals(10, query.getSize());
    }

    @Test
    void testZeroValues() {
        PageQuery query = new PageQuery(0, 0);

        assertEquals(0, query.getPage());
        assertEquals(0, query.getSize());
    }

    @Test
    void testNegativeValues() {
        PageQuery query = new PageQuery(-1, -5);

        assertEquals(-1, query.getPage());
        assertEquals(-5, query.getSize());
    }
}
