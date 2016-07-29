package org.bpunit.assertions.behaviors;

/**
 * An interface describing how a {@link org.bpunit.assertions.POJOAsserter} will behave in exceptional situations.
 */
public interface Behavior {
    void behave(String message);
}
