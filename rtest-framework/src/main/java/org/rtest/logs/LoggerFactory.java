package org.rtest.logs;

/**
 * This logger factory will be used internally
 * by the framework
 * Created by Mark Bramnik on 17/11/2016.
 *
 */
// TODO Mark: Add support for Log4j 2, common logging and Logback
public class LoggerFactory {
    private static InternalLoggerProducer loggerProducer;
    static {
        // static initialization block - we have to find the most suitable implementation
        // of logger based on what's available in a class path
        if(isSlf4JInTheClassPath()) { // its our best option
            loggerProducer = new Slf4jLoggerProducer();
        }else if(isLog4J1XInTheClassPath()) {
            loggerProducer = new Log4J1XLoggerProducer();
        } else {
            // fallback to default - java util logging - its bundled into jdk/jre
            loggerProducer = new JuliLoggerProducer();
        }
    }

    public static Logger getLogger(Class<?> cl) {
        return loggerProducer.getLogger(cl);
    }

    public static Logger getLogger(String className) {
        return loggerProducer.getLogger(className);
    }

    private static boolean isSlf4JInTheClassPath() {
        return genericUtilIsInPath("org.slf4j.Logger");
    }

    private static boolean isLog4J1XInTheClassPath() {
        return genericUtilIsInPath("org.apache.Log4j");
    }
    private static boolean genericUtilIsInPath(String s) {
        try {
            Class.forName(s);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
