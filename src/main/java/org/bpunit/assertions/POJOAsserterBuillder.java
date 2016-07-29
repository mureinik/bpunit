package org.bpunit.assertions;

import org.bpunit.utils.SeedableRandom;

import java.util.Objects;
import java.util.Random;

/**
 * A builder class for the {@link POJOAsserter} which allows creating it by a fluent API.
 */
public class POJOAsserterBuillder<T> {
    private T pojo;
    private Random random;

    public POJOAsserter<T> build() {
        Objects.requireNonNull(pojo, "Cannot construct a POJOAsserter without a POJO to assert");

        if (random == null) {
            random = new SeedableRandom();
        }

        return new POJOAsserter<>(pojo, random);
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
}
