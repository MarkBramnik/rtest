package org.rtest.framework.client;

import org.rtest.exceptions.RTestException;
import org.rtest.framework.transport.invoker.SocketSupplier;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class ClientSocketSupplier implements SocketSupplier {
    private String host;
    private int port;

    public ClientSocketSupplier(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Socket getSocket() {
        try {
            return new Socket(host, port);
        } catch (IOException e) {
            throw new RTestException("Failed to create socket for [ host : " + host + " , port : " + port + " ]", e);
        }
    }
}
