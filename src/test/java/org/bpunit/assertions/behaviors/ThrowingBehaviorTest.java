package org.bpunit.assertions.behaviors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * A test case for the {@link ThrowingBehavior} class.
 */
public class ThrowingBehaviorTest {
    @Test
    public void behave() throws Exception {
        Throwable cause = new NullPointerException("I am an NPE!");
        String message = "test message!";
        IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () -> new ThrowingBehavior().behave(message, cause));
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getCause());
    }
}
