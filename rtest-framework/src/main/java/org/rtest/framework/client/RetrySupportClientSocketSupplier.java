package org.rtest.framework.client;

import org.rtest.exceptions.RTestException;
import org.rtest.framework.common.utils.Clock;
import org.rtest.framework.common.utils.ThreadSleeper;
import org.rtest.framework.transport.invoker.SocketSupplier;
import org.rtest.logs.Logger;
import org.rtest.logs.LoggerFactory;

import java.net.ConnectException;
import java.net.Socket;

/**
 * Implements Retry mechanism with a backoff policy of period from 1 to 5 seconds with the increasing step of 1 second
 *
 * Created by Mark Bramnik on 08/01/2017.
 */
public class RetrySupportClientSocketSupplier implements SocketSupplier {
    public static final long DEFAULT_MAX_WAIT_PERIOD_MS = 60 * 1000; // 1 minute
    private static final long MAX_BACKOFF_PERIOD = 5000; // 5 seconds
    private static Logger LOG = LoggerFactory.getLogger(RetrySupportClientSocketSupplier.class);

    private SocketSupplier decoratedSocketSupplier;
    private long maxWaitPeriodInMillis;

    private Clock clock;
    private ThreadSleeper threadSleeper;
    public RetrySupportClientSocketSupplier(SocketSupplier decoratedSocketSupplier, long maxWaitPeriodInMillis, Clock clock, ThreadSleeper threadSleeper) {
        this.decoratedSocketSupplier = decoratedSocketSupplier;
        this.maxWaitPeriodInMillis = maxWaitPeriodInMillis;
        this.clock = clock;
        this.threadSleeper = threadSleeper;
    }


    @Override
    public Socket getSocket() {
        long startTime = getTime();
        long sleepBackoffPeriodMs = 0;
        do {
            try {
                return obtainSocketObject();
            } catch (RTestException ex){
                if(isFailedToConnectToRemoteServer(ex)) {
                    // if the attempt to connect has failed
                    sleepBackoffPeriodMs = calculateNextBackoffSleepPeriod(sleepBackoffPeriodMs);
                    LOG.warn("Failed to connect: " + ex.getMessage()  + " Will try again in " + sleepBackoffPeriodMs + " ms");
                    sleepThread(sleepBackoffPeriodMs);
                }
            }
        }while(!isReachedTimeout(startTime));
        throw new RTestException("Failed to connect the client. Timeout of "  + maxWaitPeriodInMillis + " ms. has been reached");
    }

    private boolean isFailedToConnectToRemoteServer(RTestException ex) {
        return ex.getCause() instanceof ConnectException;
    }

    private Socket obtainSocketObject() {
        Socket socket = decoratedSocketSupplier.getSocket();
        LOG.debug("Connection to server established");
        return socket;
    }

    private boolean isReachedTimeout(long startTime) {
        return getTime() - startTime >= maxWaitPeriodInMillis;
    }

    private long calculateNextBackoffSleepPeriod(long currentPeriod) {
        if(currentPeriod < MAX_BACKOFF_PERIOD) {
           return currentPeriod + 1000;
        }
        else {
            return currentPeriod;
        }
    }
    private void sleepThread(long millis) {
        threadSleeper.sleep(millis);
    }

    private long getTime() {
        return clock.getCurrentTime();
    }
}
