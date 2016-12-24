package org.rtest.logs;

/**
 * Since we don't really know which logger implementations are available
 * in runtime, we'll find the most suitable implementation
 * Created by Mark Bramnik on 17/11/2016.
 */
public interface Logger {
    void debug(String message);
    void debug(String message, Throwable t);
    void info(String message);
    void info(String message, Throwable t);
    void warn(String message);
    void warn(String message, Throwable t);
    void error(String message);
    void error(String message, Throwable t);

}
