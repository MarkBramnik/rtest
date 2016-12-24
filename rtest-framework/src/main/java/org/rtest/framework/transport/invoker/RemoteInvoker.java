package org.rtest.framework.transport.invoker;

import org.rtest.framework.transport.commands.RTestData;

/**
 * Created by Mark Bramnik on 14/09/2016.
 */
public interface RemoteInvoker {

    void send(RTestData request);
    RTestData read();
    void close();
}
