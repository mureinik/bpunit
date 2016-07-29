package org.bpunit.assertions;

import org.bpunit.examples.ObjectRandom;
import org.bpunit.examples.SomeClass;
import org.junit.Test;

/**
 * A test case for the {@link POJOAsserterBuillder}.
 */
public class POJOAsserterBuillderTest {
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
}