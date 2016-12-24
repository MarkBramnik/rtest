package org.rtest.framework.server;

import org.rtest.framework.transport.invoker.SocketSupplier;

import java.net.Socket;

/**
 * Created by Mark Bramnik on 19/09/2016.
 */
public class ServerSocketSupplier implements SocketSupplier {
    private Socket socket;

    public ServerSocketSupplier(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }
}
