package org.bpunit.assertions;

import org.bpunit.assertions.behaviors.Behavior;
import org.bpunit.assertions.behaviors.LoggingBehavior;
import org.bpunit.utils.SeedableRandom;

import java.util.Objects;
import java.util.Random;

/**
 * A builder class for the {@link POJOAsserter} which allows creating it by a fluent API.
 */
public class POJOAsserterBuillder<T> {
    private T pojo;
    private Random random;
    private Behavior noGetterBehavior;

    public POJOAsserter<T> build() {
        Objects.requireNonNull(pojo, "Cannot construct a POJOAsserter without a POJO to assert");

        if (random == null) {
            random = new SeedableRandom();
        }

        if (noGetterBehavior == null) {
            noGetterBehavior = new LoggingBehavior();
        }

        return new POJOAsserter<>(pojo, random, noGetterBehavior);
    }

    /**
     * Specify the POJO for which to create a {@link POJOAsserterBuillder}.
     *
     * @param pojo
     *          The POJO to be asserted.
     */
    public POJOAsserterBuillder forPOJO(T pojo) {
        this.pojo = pojo;
        return this;
    }

    /**
     * Specify the {@link Random} generator to use when asserting the given POJO.
     * If this method is not called, a default {@link org.bpunit.utils.SeedableRandom} with its default seed is used.
     *
     * @param random
     *          The random generator to use.
     */
    public POJOAsserterBuillder withRandom(Random random) {
        this.random = random;
        return this;
    }

    /**
     * Specify the {@link Behavior} to use when a property doesn't have a pair of getter and setter.
     * If this method is not called, a default {@link LoggingBehavior} is used.
     *
     * @param noGetterBehavior
     *          The Behavior to use
     */
    public POJOAsserterBuillder withNoGetterBehavior(Behavior noGetterBehavior) {
        this.noGetterBehavior = noGetterBehavior;
        return this;
    }
}
