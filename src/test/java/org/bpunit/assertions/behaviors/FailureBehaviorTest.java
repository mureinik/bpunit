package org.bpunit.assertions.behaviors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.startsWith;

/**
 * A test case for the {@link FailureBehaviorTest} class.
 */
public class FailureBehaviorTest {
    private static final String MESSAGE = "A message in a bottle";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Behavior behavior;

    @Before
    public void setUp() {
        expectedException.expect(AssertionError.class);
        behavior = new FailureBehavior();
    }

    @Test
    public void noThrowable() {
        expectedException.expectMessage(MESSAGE);
        behavior.behave(MESSAGE, null);
    }

    @Test
    public void withThrowable() {
        expectedException.expectMessage(startsWith(MESSAGE));
        behavior.behave(MESSAGE, new NullPointerException());
    }
}