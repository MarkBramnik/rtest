package org.rtest.framework.transport.commands;

/**
 * Created by Mark Bramnik on 03/11/2016.
 */
public class TestReportGeneratedData extends AbstractIdSupportData {

    private String testClass;
    private byte [] report;

    public TestReportGeneratedData(String id, String testClass, byte[] report) {
        super(id);
        this.testClass = testClass;
        this.report = report;
    }

    public String getTestClass() {
        return testClass;
    }

    public byte[] getReport() {
        return report;
    }
}
