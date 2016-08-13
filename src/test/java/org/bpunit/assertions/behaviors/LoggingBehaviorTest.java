package org.bpunit.assertions.behaviors;

import org.junit.Test;

/**
 * A basic test case for the {@link LoggingBehavior} class.
 * At this point, it just tests the behavior does not fail.
 * If in the future some mocking mechanism is introduced to the project, this test should be expanded
 */
public class LoggingBehaviorTest {
    @Test
    public void behaveDefault() {
        Behavior behavior = new LoggingBehavior();
        behavior.behave("message", null);
    }

    @Test
    public void behaveNoStackTrace() {
        Behavior behavior = new LoggingBehavior(false);
        behavior.behave("message", null);
    }


    @Test
    public void behaveWithStackTrace() {
        Behavior behavior = new LoggingBehavior(true);
        behavior.behave("message", null);
    }
}
