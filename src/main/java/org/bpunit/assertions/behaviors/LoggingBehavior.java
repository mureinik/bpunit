package org.bpunit.assertions.behaviors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Behavior} that just logs and allows the flow to continue.
 */
public class LoggingBehavior implements Behavior {

    private static final Logger log = LoggerFactory.getLogger(LoggingBehavior.class);

    private boolean showStackTrace;

    public LoggingBehavior() {
        this(true);
    }

    public LoggingBehavior(boolean showStackTrace) {
        this.showStackTrace = showStackTrace;
    }

    @Override
    public void behave(String message, Throwable t) {
        if (showStackTrace) {
            log.info(message, t);
        } else {
            log.info(message);
        }
    }
}
