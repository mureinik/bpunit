package org.bpunit.assertions;

import org.bpunit.assertions.behaviors.Behavior;
import org.bpunit.examples.ObjectRandom;
import org.bpunit.examples.SomeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * A test case for the {@link POJOAsserterBuillder}.
 */
public class POJOAsserterBuillderTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String BEHAVIOR_ERROR_MESSAGE = "Test error message for FAILING_BEHAVIOR";

    private static final Behavior FAILING_BEHAVIOR = new Behavior() {
        @Override
        public void behave(String message) {
            throw new IllegalArgumentException(BEHAVIOR_ERROR_MESSAGE);
        }
    };

    @Test
    public void testWithRandom() {
        POJOAsserter pojoAsserter =
                new POJOAsserterBuillder<>().forPOJO(new SomeClass()).withRandom(new ObjectRandom()).build();
        pojoAsserter.assertProperties();
    }

    @Test
    public void testHappyPath() {
        POJOAsserter pojoAsserter = new POJOAsserterBuillder<>().forPOJO(new SomeClass()).build();
        pojoAsserter.assertProperties();
    }

    @Test(expected = NullPointerException.class)
    public void testWithoutPOJO() {
        POJOAsserter pojoAsserter = new POJOAsserterBuillder<>().build();
        pojoAsserter.assertProperties();
    }

    @Test
    public void testWithBehavior() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(BEHAVIOR_ERROR_MESSAGE);
        POJOAsserter pojoAsserter =
                new POJOAsserterBuillder<>().forPOJO(new SomeClass()).withNoGetterBehavior(FAILING_BEHAVIOR).build();
        pojoAsserter.assertProperties();
    }
}