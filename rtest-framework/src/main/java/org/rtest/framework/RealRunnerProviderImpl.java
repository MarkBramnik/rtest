package org.rtest.framework;

import org.junit.runner.Runner;
import org.rtest.framework.client.ClientSideInternalRemoteRunner;
import org.rtest.framework.client.ClientSocketSupplier;
import org.rtest.framework.config.props.SupportedConfigurationProperties;
import org.rtest.framework.transport.invoker.DefaultRemoteInvoker;
import org.rtest.framework.server.ServerSideInternalRemoteRunner;

/**
 * Created by Mark Bramnik on 15/09/2016.
 */
public class RealRunnerProviderImpl implements RealRunnerProvider {
    @Override
    public Runner getClientRunner(Class<?> testClass) {
        String  serverHost = System.getProperty(SupportedConfigurationProperties.Client.SERVER_HOST, "localhost");
        Integer serverPort = Integer.parseInt(System.getProperty(SupportedConfigurationProperties.Client.SERVER_PORT, "7890"));

        ClientSocketSupplier clientSocketSupplier = new ClientSocketSupplier(serverHost, serverPort);
        DefaultRemoteInvoker remoteInvoker = new DefaultRemoteInvoker(clientSocketSupplier);
        ClientSideInternalRemoteRunner runner = new ClientSideInternalRemoteRunner(testClass, remoteInvoker);
        runner.init();
        return runner;
    }
}
