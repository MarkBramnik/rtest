package org.rtest.framework.server.reports;

/**
 * Created by Mark Bramnik on 04/11/2016.
 */
public interface ServerReportProcessor {

    byte [] readReport(String testClassName);

}
