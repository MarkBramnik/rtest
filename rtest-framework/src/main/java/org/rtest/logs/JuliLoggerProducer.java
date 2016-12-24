package org.rtest.logs;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
public class JuliLoggerProducer implements InternalLoggerProducer {

    @Override
    public Logger getLogger(Class<?> cl) {
        return new JuliLoggerAdapter(cl);
    }

    @Override
    public Logger getLogger(String className) {
        return new JuliLoggerAdapter(className);
    }
}
