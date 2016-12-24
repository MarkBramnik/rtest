package org.rtest.logs;

import org.apache.log4j.LogManager;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
public class Log4J1XLoggerAdapter implements Logger {
    private org.apache.log4j.Logger LOG;
    public Log4J1XLoggerAdapter(Class<?> cl) {
        LOG = LogManager.getLogger(cl);
    }

    public Log4J1XLoggerAdapter(String className) {
        LOG = LogManager.getLogger(className);
    }
    @Override
    public void debug(String message) {
        LOG.debug(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        LOG.debug(message, t);
    }

    @Override
    public void info(String message) {
        LOG.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        LOG.info(message, t);
    }

    @Override
    public void warn(String message) {
        LOG.warn(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        LOG.warn(message, t);
    }

    @Override
    public void error(String message) {
        LOG.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        LOG.error(message, t);
    }
}
