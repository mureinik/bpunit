package org.bpunit.assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bpunit.assertions.behaviors.Behavior;
import org.bpunit.examples.ObjectRandom;
import org.bpunit.examples.SomeClass;
import org.bpunit.examples.SomeClassWithThrowingSetter;
import org.junit.jupiter.api.Test;

/**
 * A test case for the {@link POJOAsserterBuillder}.
 */
public class POJOAsserterBuillderTest {
    private static final String BEHAVIOR_ERROR_MESSAGE = "Test error message for FAILING_BEHAVIOR";

    private static final Behavior FAILING_BEHAVIOR = (message, t) -> {
        throw new IllegalArgumentException(BEHAVIOR_ERROR_MESSAGE);
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

    @Test
    public void testWithoutPOJO() {
        assertThrows(NullPointerException.class, () -> new POJOAsserterBuillder<>().build());
    }

    @Test
    public void testWithNoGetterBehavior() {
        POJOAsserter pojoAsserter =
                new POJOAsserterBuillder<>().forPOJO(new SomeClass()).withNoGetterBehavior(FAILING_BEHAVIOR).build();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, pojoAsserter::assertProperties);
        assertEquals(BEHAVIOR_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void testWithRandomFailureBehavior() {
        POJOAsserter pojoAsserter =
                new POJOAsserterBuillder<>().forPOJO(new SomeClass()).withRandomFailureBehavior(FAILING_BEHAVIOR).build();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, pojoAsserter::assertProperties);
        assertEquals(BEHAVIOR_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void testWithPropertyTestFailureBehavior() {
        POJOAsserter pojoAsserter =
                new POJOAsserterBuillder<>().forPOJO(new SomeClassWithThrowingSetter())
                        .withPropertyTestFailureBehavior(FAILING_BEHAVIOR).build();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, pojoAsserter::assertProperties);
        assertEquals(BEHAVIOR_ERROR_MESSAGE, e.getMessage());
    }
}
