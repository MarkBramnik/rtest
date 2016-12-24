package org.rtest.logs;

import org.slf4j.LoggerFactory;

/**
 * An adapter for Slf4j framework
 * Created by Mark Bramnik on 17/11/2016.
 */
public class Slf4JLoggerAdapter implements Logger {
    private org.slf4j.Logger LOG;
    public Slf4JLoggerAdapter(Class<?> cl) {
        LOG = LoggerFactory.getLogger(cl);
    }

    public Slf4JLoggerAdapter(String name) {
        LOG = LoggerFactory.getLogger(name);
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
