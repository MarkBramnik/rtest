package org.rtest.logs;

import java.util.logging.LogManager;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
// TODO Mark: figure out what to do with Exceptions since the logger doesn't support them
public class JuliLoggerAdapter implements Logger {
    private java.util.logging.Logger LOG;
    public JuliLoggerAdapter(Class<?> cl) {
        this(cl.getName());
    }

    public JuliLoggerAdapter(String name) {
        LOG = LogManager.getLogManager().getLogger(name);
    }

    @Override
    public void debug(String message) {
        LOG.finest(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        LOG.finest(message);
    }

    @Override
    public void info(String message) {
        LOG.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        LOG.info(message);
    }

    @Override
    public void warn(String message) {
        LOG.warning(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        LOG.warning(message);
    }

    @Override
    public void error(String message) {
        LOG.severe(message);
    }

    @Override
    public void error(String message, Throwable t) {
        LOG.severe(message);
    }
}
