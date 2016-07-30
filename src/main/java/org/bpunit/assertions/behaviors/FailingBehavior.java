package org.bpunit.assertions.behaviors;

import static org.junit.Assert.fail;

/**
 * A {@link Behavior} that fails the test.
 */
public class FailingBehavior implements Behavior {
    @Override
    public void behave(String message, Throwable t) {
        if (t != null) {
            fail(message + " due to exception: " + t);
        } else {
            fail(message);
        }
    }
}
