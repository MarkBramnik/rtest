package org.rtest.framework.common.data;

import org.junit.ComparisonFailure;

import java.io.Serializable;

/**
 * Created by Mark Bramnik on 21/09/2016.
 */
public class ComparisonFailureContainer extends ComparisonFailure implements Serializable {

    private Throwable cause;
    public ComparisonFailureContainer(String message, String expected, String actual, Throwable cause, StackTraceElement[] stackTraceElements) {
        super(message, expected, actual);
        this.cause = cause;
        setStackTrace(stackTraceElements);
    }



    @Override
    public synchronized Throwable getCause() {
        return cause;
    }
}
