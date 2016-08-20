package org.bpunit.assertions.behaviors;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * A basic test case for the {@link LoggingBehavior} class.
 * At this point, it just tests the behavior does not fail.
 * If in the future some mocking mechanism is introduced to the project, this test should be expanded
 */
@RunWith(Theories.class)
public class LoggingBehaviorTest {

    @DataPoints
    public static LoggingBehavior[] BEHAVIORS =
            {new LoggingBehavior(), new LoggingBehavior(true), new LoggingBehavior(false)};

    @Theory
    public void behavae(LoggingBehavior behavior) {
        behavior.behave("message", null);
    }
}
