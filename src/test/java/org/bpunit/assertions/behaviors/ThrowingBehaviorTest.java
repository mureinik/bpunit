package org.bpunit.assertions.behaviors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

/**
 * A test case for the {@link ThrowingBehavior} class.
 */
public class ThrowingBehaviorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void behave() throws Exception {
        Throwable cause = new NullPointerException("I am an NPE!");
        String message = "test message!";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(message);
        expectedException.expectCause(is(cause));
        new ThrowingBehavior().behave(message, cause);
    }
}
