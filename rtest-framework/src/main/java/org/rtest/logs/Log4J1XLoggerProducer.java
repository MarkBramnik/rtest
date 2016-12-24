package org.rtest.logs;

import org.apache.log4j.LogManager;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
public class Log4J1XLoggerProducer implements InternalLoggerProducer {
    @Override
    public Logger getLogger(Class<?> cl) {
        return new Log4J1XLoggerAdapter(cl);
    }

    @Override
    public Logger getLogger(String className) {
        return new Log4J1XLoggerAdapter(className);
    }
}
