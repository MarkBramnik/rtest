package org.rtest.framework.server;

import org.rtest.exceptions.RTestException;
import org.rtest.framework.server.reports.DefaultServerReportProcessorImpl;
import org.rtest.framework.transport.invoker.DefaultRemoteInvoker;
import org.rtest.logs.Logger;
import org.rtest.logs.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The entry point of the server side integration.
 * The server side application should create the instance of this object in order to get requests about test execution
 * </p>
 * In order to run the server the method  {@linkplain RTestRemoteServer#start()} method must be invoked
 * <br/>
 * In order to stop the server the method {@linkplain RTestRemoteServer#shutdown()} method must be invoked
 * Created by Mark Bramnik on 18/09/2016.
 */
public class RTestRemoteServer {
    private Integer port;
    private Integer threadPoolSize;
    private ExecutorService executorService;
    private volatile boolean shouldProcessRequests = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(RTestRemoteServer.class);

    /**
     * Create the server
     * @param port - the port to be set up on
     * @param threadPoolSize - the number of threads that will be in the pool. Each thread will be able to execute one test at a time
     *
     */
    public RTestRemoteServer(Integer port, Integer threadPoolSize) {
        this.port = port;
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * Starts the testing server
     */
    public void start() {

        executorService = Executors.newFixedThreadPool(threadPoolSize);
        LOGGER.info("The Testing service was started on port "+ port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(shouldProcessRequests) {
                Socket socket = serverSocket.accept();
                executorService.execute(
                        new ServerTestProcessor(
                                new DefaultRemoteInvoker(
                                        new ServerSocketSupplier(socket)
                                ),
                                new DefaultServerReportProcessorImpl()
                        )
                );
            }
        } catch (IOException e) {
            throw new RTestException("Failed to proceed test", e);
        }
    }

    /**
     * Shuts down the testing server
     * All tests will finish
     */
    public void shutdown() {
        shouldProcessRequests = false;
        executorService.shutdown();
    }

    /**
     * Shuts down the testing server
     * All executing tests will be attempted to be stopped
     */
    public void shutdownNow() {
        shouldProcessRequests = false;
        executorService.shutdownNow();
    }

}
