package org.bpunit.assertions.behaviors;

/**
 * A {@link Behavior} that throws an {@link IllegalArgumentException} when triggered.
 */
public class ThrowingBehavior implements Behavior {
    @Override
    public void behave(String message) {
        throw new IllegalArgumentException(message);
    }
}
