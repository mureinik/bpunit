package org.bpunit.assertions.behaviors;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * A basic test case for the {@link LoggingBehavior} class.
 * At this point, it just tests the behavior does not fail.
 * If in the future some mocking mechanism is introduced to the project, this test should be expanded
 */
public class LoggingBehaviorTest {

    private static class BehaviorsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of
                    (new LoggingBehavior(),new LoggingBehavior(true), new LoggingBehavior(false))
                    .map(Arguments::of);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BehaviorsProvider.class)
    public void behavae(LoggingBehavior behavior) {
        behavior.behave("message", null);
    }
}
