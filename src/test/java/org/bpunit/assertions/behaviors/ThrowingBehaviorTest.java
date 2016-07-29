package org.bpunit.assertions.behaviors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * A test case for the {@link ThrowingBehavior} class.
 */
public class ThrowingBehaviorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void behave() throws Exception {
        String message = "test message!";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(message);
        new ThrowingBehavior().behave(message);
    }
}
