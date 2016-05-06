package org.bpunit.examples;

/**
 * A class with a setter that fails with an exception, to test that {@link org.bpunit.assertions.AssertUtils} properly
 * fails with it.
 */
public class SomeClassWithThrowingSetter {
    public int getSomeProperty() {
        return 0;
    }

    public void setSomeProperty(int ignore) {
        throw new IllegalArgumentException();
    }
}
