package org.bpunit.examples;

import org.bpunit.utils.SeedableRandom;

import java.util.Date;

/**
 * A random-value generator that provides a "randomizer" for an {@link Object}, used for testing.
 */
public class ObjectRandom extends SeedableRandom {
    public ObjectRandom() {
        super(123L);
    }

    public Object nextObject() {
        return new Object();
    }

    /** Intentionally misleading name */
    public Date nextSimpleDateFormat() {
        return new Date();
    }
}
