package org.rtest.framework.config.props;

/**
 * Denotes all the supported configuration properties
 * Created by Mark Bramnik on 17/11/2016.
 */
public interface SupportedConfigurationProperties {
    interface Client {
        String ASK_SERVER_FOR_REPORTS = "rtest.client.askServerForReports";
        String SERVER_PORT = "rtest.client.serverPort";
        String SERVER_HOST = "rtest.client.serverHost";
        String REPORT_DIR  = "rtest.client.reportDir";
        String MAX_CONNECTION_WAIT_PERIOD = "rtest.client.maxConnectionWaitPeriodMs";
    }
}
