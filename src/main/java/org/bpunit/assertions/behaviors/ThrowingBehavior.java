package org.bpunit.assertions.behaviors;

/**
 * A {@link Behavior} that throws an {@link IllegalArgumentException} when triggered.
 */
public class ThrowingBehavior implements Behavior {
    @Override
    public void behave(String message , Throwable t) {
        throw new IllegalArgumentException(message, t);
    }
}
