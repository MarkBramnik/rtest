package org.rtest.testapp.webapp.server.embedded.main;

import org.rtest.framework.server.RTestRemoteServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Mark Bramnik on 08/11/2016.
 */
public class RTestIntegrationListener implements ServletContextListener {
    private RTestRemoteServer remoteServer;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Application Context initialized. Starting Remote Testing Utility on port 7890");
        remoteServer = new RTestRemoteServer(7890, 5);
        remoteServer.start();
        System.out.println("Remote Test Utility is Started");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Application Context destroyed");
        remoteServer.shutdown();
    }
}
