package org.bpunit.assertions.behaviors;

import org.junit.Test;

/**
 * A basic test case for the {@link LoggingBehavior} class.
 * At this point, it just tests the behavior does not fail.
 * If in the future some mocking mechanism is introduced to the poject, this test should be expanded
 */
public class LoggingBehaviorTest {
    @Test
    public void behave() {
        Behavior behavior = new LoggingBehavior();
        behavior.behave("message", null);
    }
}
