package org.bpunit.assertions;

import org.bpunit.assertions.behaviors.FailingBehavior;
import org.bpunit.assertions.behaviors.LoggingBehavior;
import org.bpunit.examples.ObjectRandom;
import org.bpunit.examples.SomeClass;
import org.bpunit.examples.SomeClassWithThrowingSetter;
import org.bpunit.utils.SeedableRandom;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * A test case for the {@link POJOAsserter}.
 */
public class POJOAsserterTest {
    @Test
    public void testSeedableRandom() {
        assentSimpleClass(new SeedableRandom(System.currentTimeMillis()), false);
    }

    @Test
    public void testPrivateRandomizer() {
        assentSimpleClass(new ObjectRandom(), true);
    }

    @Test(expected = AssertionError.class)
    public void testThrowingSetter() {
        AssertUtils.testProperties(new SomeClassWithThrowingSetter());
    }

    private static void assentSimpleClass(Random random, boolean canRandomizeObject) {
        SomeClass sc = new SomeClass();
        new POJOAsserter<>(sc, random, new LoggingBehavior(), new LoggingBehavior(), new FailingBehavior())
                .assertProperties();

        // Make sure all properties were addressed
        assertNotNull("myInt not checked", sc.getMyInt());
        assertNotNull("myLong not checked", sc.getMyDate());
        assertNotNull("myString not checked", sc.getMyString());
        assertNotNull("someBoolean not checked", sc.getSomeBoolean());
        assertNotNull("someOtherBoolean not checked", sc.isSomeOtherBoolean());
        assertFalse("primitiveDouble not checked", "NaN".equals(String.valueOf(sc.getMyPrimitiveDouble())));
        assertEquals("Wrong ability to randomize Object", canRandomizeObject, sc.getMyObject() != null);
    }
}
