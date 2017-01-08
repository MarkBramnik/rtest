package org.rtest.framework.common.utils;

import org.rtest.exceptions.RTestException;

/**
 * Created by Mark Bramnik on 09/01/2017.
 */
public class DefaultThreadSleeper implements ThreadSleeper {
    @Override
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RTestException("Failed to wait for " + millis + " ms.", e);
        }
    }
}
