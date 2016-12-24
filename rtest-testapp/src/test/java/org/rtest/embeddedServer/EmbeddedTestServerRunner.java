package org.rtest.embeddedServer;

import org.rtest.framework.server.RTestRemoteServer;

/**
 * Created by Mark Bramnik on 08/09/2016.
 */
public class EmbeddedTestServerRunner {
    public static void main(String[] args) {
        System.setProperty("rtest.mode", "server");
        RTestRemoteServer remoteServer = new RTestRemoteServer(7890, 1);
        remoteServer.start();
    }
}
