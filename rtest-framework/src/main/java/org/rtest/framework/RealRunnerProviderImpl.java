package org.rtest.framework;

import org.junit.runner.Runner;
import org.rtest.framework.client.ClientSideInternalRemoteRunner;
import org.rtest.framework.client.ClientSocketSupplier;
import org.rtest.framework.client.RetrySupportClientSocketSupplier;
import org.rtest.framework.common.utils.DefaultClock;
import org.rtest.framework.common.utils.DefaultThreadSleeper;
import org.rtest.framework.config.props.SupportedConfigurationProperties;
import org.rtest.framework.transport.invoker.DefaultRemoteInvoker;
import org.rtest.framework.server.ServerSideInternalRemoteRunner;
import org.rtest.framework.transport.invoker.SocketSupplier;

/**
 * Created by Mark Bramnik on 15/09/2016.
 */
public class RealRunnerProviderImpl implements RealRunnerProvider {
    @Override
    public Runner getClientRunner(Class<?> testClass) {
        String  serverHost = System.getProperty(SupportedConfigurationProperties.Client.SERVER_HOST, "localhost");
        Integer serverPort = Integer.parseInt(System.getProperty(SupportedConfigurationProperties.Client.SERVER_PORT, "7890"));

        SocketSupplier clientSocketSupplier = new RetrySupportClientSocketSupplier(
                new ClientSocketSupplier(serverHost, serverPort),
                Long.parseLong(
                        System.getProperty(SupportedConfigurationProperties.Client.MAX_CONNECTION_WAIT_PERIOD,
                                           String.valueOf(RetrySupportClientSocketSupplier.DEFAULT_MAX_WAIT_PERIOD_MS))),
                new DefaultClock(),
                new DefaultThreadSleeper()
        );
        DefaultRemoteInvoker remoteInvoker = new DefaultRemoteInvoker(clientSocketSupplier);
        ClientSideInternalRemoteRunner runner = new ClientSideInternalRemoteRunner(testClass, remoteInvoker);
        runner.init();
        return runner;
    }
}
