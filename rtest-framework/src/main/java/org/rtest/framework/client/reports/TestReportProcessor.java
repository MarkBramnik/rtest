package org.rtest.framework.client.reports;

/**
 * Defines the protocol for handling the reports on client side
 * Created by Mark Bramnik on 03/11/2016.
 */
public interface TestReportProcessor {
    void processData(String testClass, byte[] report);
}
