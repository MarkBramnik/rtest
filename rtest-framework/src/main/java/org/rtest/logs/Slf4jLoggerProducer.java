package org.rtest.logs;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
public class Slf4jLoggerProducer implements InternalLoggerProducer {
    @Override
    public Logger getLogger(Class<?> cl) {
        return new Slf4JLoggerAdapter(cl);
    }

    @Override
    public Logger getLogger(String className) {
        return new Slf4JLoggerAdapter(className);
    }
}
