package org.rtest.exceptions;

/**
 * Created by Mark Bramnik on 06/09/2016.
 */
public class RTestException extends RuntimeException {
    public RTestException() {
    }

    public RTestException(String message) {
        super(message);
    }

    public RTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RTestException(Throwable cause) {
        super(cause);
    }

    public RTestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
