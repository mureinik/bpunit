package org.bpunit.assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bpunit.assertions.behaviors.FailingBehavior;
import org.bpunit.assertions.behaviors.LoggingBehavior;
import org.bpunit.examples.ObjectRandom;
import org.bpunit.examples.SomeClass;
import org.bpunit.examples.SomeClassWithThrowingSetter;
import org.bpunit.utils.SeedableRandom;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Random;

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

    @Test
    public void testThrowingSetter() {
        assertThrows(AssertionFailedError.class, () -> AssertUtils.testProperties(new SomeClassWithThrowingSetter()));
    }

    private static void assentSimpleClass(Random random, boolean canRandomizeObject) {
        SomeClass sc = new SomeClass();
        new POJOAsserter<>(sc, random, new LoggingBehavior(false), new LoggingBehavior(false), new FailingBehavior())
                .assertProperties();

        // Make sure all properties were addressed
        assertNotNull(sc.getMyInt(), "myInt not checked");
        assertNotNull(sc.getMyDate(), "myLong not checked");
        assertNotNull(sc.getMyString(), "myString not checked");
        assertNotNull(sc.getSomeBoolean(), "someBoolean not checked");
        assertNotNull(sc.isSomeOtherBoolean(), "someOtherBoolean not checked");
        assertFalse("NaN".equals(String.valueOf(sc.getMyPrimitiveDouble())), "primitiveDouble not checked");
        assertEquals(canRandomizeObject, sc.getMyObject() != null, "Wrong ability to randomize Object");
    }
}
