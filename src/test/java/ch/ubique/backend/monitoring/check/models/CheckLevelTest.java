package ch.ubique.backend.monitoring.check.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CheckLevelTest {

    @Test
    void isSameOrMoreSevereThan() {
        assertTrue(CheckLevel.ERROR.isSameOrMoreSevereThan(CheckLevel.ERROR));
        assertTrue(CheckLevel.ERROR.isSameOrMoreSevereThan(CheckLevel.WARNING));
        assertTrue(CheckLevel.ERROR.isSameOrMoreSevereThan(CheckLevel.INFO));

        assertFalse(CheckLevel.WARNING.isSameOrMoreSevereThan(CheckLevel.ERROR));
        assertTrue(CheckLevel.WARNING.isSameOrMoreSevereThan(CheckLevel.WARNING));
        assertTrue(CheckLevel.WARNING.isSameOrMoreSevereThan(CheckLevel.INFO));

        assertFalse(CheckLevel.INFO.isSameOrMoreSevereThan(CheckLevel.ERROR));
        assertFalse(CheckLevel.INFO.isSameOrMoreSevereThan(CheckLevel.WARNING));
        assertTrue(CheckLevel.INFO.isSameOrMoreSevereThan(CheckLevel.INFO));
    }
}
