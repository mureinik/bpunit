package org.bpunit.assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.bpunit.examples.ObjectRandom;
import org.bpunit.examples.SomeClass;
import org.bpunit.examples.SomeClassWithThrowingSetter;
import org.bpunit.utils.SeedableRandom;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * A test case for {@link AssertUtils}
 */
public class AssertUtilsTest {
    @Test
    public void testDefaultRandom() {
        assentSimpleClass(null, false);
    }

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
        if (random != null) {
            AssertUtils.testProperties(sc, random);
        } else {
            AssertUtils.testProperties(sc);
        }

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
