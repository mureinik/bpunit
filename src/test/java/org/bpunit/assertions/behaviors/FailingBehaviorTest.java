package org.bpunit.assertions.behaviors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * A test case for the {@link FailingBehaviorTest} class.
 */
public class FailingBehaviorTest {
    private static final String MESSAGE = "A message in a bottle";

    private Behavior behavior;

    @BeforeEach
    public void setUp() {
        behavior = new FailingBehavior();
    }

    @Test
    public void noThrowable() {
        AssertionFailedError asf = assertThrows(AssertionFailedError.class, () -> behavior.behave(MESSAGE, null));
        assertEquals(MESSAGE, asf.getMessage());
    }

    @Test
    public void withThrowable() {
        AssertionFailedError asf = assertThrows(AssertionFailedError.class, () -> behavior.behave(MESSAGE, new NullPointerException()));
        assertTrue(asf.getMessage().startsWith(MESSAGE));
    }
}
