package org.rtest.logs;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
public interface InternalLoggerProducer {
    Logger getLogger(Class<?> cl);
    Logger getLogger(String className);
}
