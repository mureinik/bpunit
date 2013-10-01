package org.bpunit.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.bpunit.examples.SomeClass;
import org.bpunit.utils.SeedableRandom;
import org.junit.Test;

/**
 * A test case for {@link AssertUtils}
 */
public class AssertUtilsTest {
    private static class ObjectRandom extends SeedableRandom {
        public ObjectRandom() {
            super(123L);
        }

        public Object nextObject() {
            return new Object();
        }
    }

    @Test
    public void testSimpleClass() {
        assentSimpleClass(new SeedableRandom(System.currentTimeMillis()), false);
    }

    @Test
    public void testPrivateRandomizer() {
        assentSimpleClass(new ObjectRandom(), true);
    }

    private static void assentSimpleClass(Random random, boolean canRandomizeObject) {
        SomeClass sc = new SomeClass();
        AssertUtils.testProperties(sc, random);

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
