package org.rtest.framework.common.utils;

/**
 * A default implementation of clock interface
 * Created by Mark Bramnik on 09/01/2017.
 */
public class DefaultClock implements Clock {
    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
