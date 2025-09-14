package com.crediya.apply.api.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplyPathTest {

    @Test
    void testDefaultValues() {
        ApplyPath path = new ApplyPath();

        assertNull(path.getSaveApply());
        assertNull(path.getListApplys());
    }

    @Test
    void testSettersAndGetters() {
        ApplyPath path = new ApplyPath();

        path.setSaveApply("/api/v1/apply");
        path.setListApplys("/api/v1/apply/list");

        assertEquals("/api/v1/apply", path.getSaveApply());
        assertEquals("/api/v1/apply/list", path.getListApplys());
    }

    @Test
    void testOverrideValues() {
        ApplyPath path = new ApplyPath();
        path.setSaveApply("/custom/save");
        path.setListApplys("/custom/list");

        assertEquals("/custom/save", path.getSaveApply());
        assertEquals("/custom/list", path.getListApplys());
    }
}
