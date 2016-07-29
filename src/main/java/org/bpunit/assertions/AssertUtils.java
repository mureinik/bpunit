package org.bpunit.assertions;

import org.bpunit.utils.SeedableRandom;

import java.util.Random;

/**
 * A utility class for boilerplate assertions
 */
public class AssertUtils {
    /** Should not be initialized. */
    private AssertUtils() {
    }

    /**
     * Tests that the {@code getXYZ()} and {@code setXYZ(SomeType XYZ)} methods of {@code o} are symmetric. I.e., If
     * {@code setXYZ} is called with some randomly generated value, the subsequent {@code getXYZ} will return the same
     * value.
     *
     * This behavior is tested by calling {@link org.junit.Assert}'s assertions, so a failure would behave just like any
     * other JUnit test failure.
     *
     * Random values are generated by BPUnit's default randomizer, {@link SeedableRandom}.
     *
     * @param o
     *            The object to test.
     */
    public static void testProperties(Object o) {
        testProperties(o, new SeedableRandom(System.currentTimeMillis()));
    }

    /**
     * Tests that the {@code getXYZ()} and {@code setXYZ(SomeType XYZ)} methods of {@code o} are symmetric. I.e., If
     * {@code setXYZ} is called with some randomly generated value, the subsequent {@code getXYZ} will return the same
     * value.
     *
     * This behavior is tested by calling {@link org.junit.Assert}'s assertions, so a failure would behave just like any
     * other JUnit test failure.
     *
     * @param o
     *            The object to test.
     * @param random
     *            An instance of {@link Random} used to randomize values for {@code o}'s properties.
     *            {@code random} should have a public method called {@code nextXYZ()} which takes no arguments and
     *            returns an instance of {@code XYZ} for each type of property {@code o} has.
     */
    public static void testProperties(Object o, Random random) {
        new POJOAsserterBuillder<>().forPOJO(o).withRandom(random).build().assertProperties();
    }
}
