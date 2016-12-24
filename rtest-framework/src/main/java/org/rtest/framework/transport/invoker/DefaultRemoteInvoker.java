package org.rtest.framework.transport.invoker;

import org.rtest.exceptions.RTestException;
import org.rtest.framework.transport.commands.RTestData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Mark Bramnik on 17/09/2016.
 */
public class DefaultRemoteInvoker implements RemoteInvoker {

    private Socket              socket;
    private ObjectOutputStream  out;
    private ObjectInputStream   in;

    public DefaultRemoteInvoker(SocketSupplier socketSupplier) {
        try {
            socket       = socketSupplier.getSocket();
            out          = new ObjectOutputStream(socket.getOutputStream());
            in           = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RTestException("Failed to create socket streams", e);
        }
    }

    @Override
    public void send(RTestData request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            throw new RTestException("Failed to send an object of type: " +request.getClass().getName(), e);
        }
    }

    @Override
    public RTestData read() {
        try {
            return (RTestData) in.readObject();

        } catch (IOException  | ClassNotFoundException e) {
            throw new RTestException("Failed to read object form the input stream", e);
        }
    }

    @Override
    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
           throw new RTestException("Failed to close data channel", e);
        }


    }
}
