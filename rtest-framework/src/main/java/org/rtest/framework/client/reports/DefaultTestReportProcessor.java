package org.rtest.framework.client.reports;

import org.rtest.framework.config.props.SupportedConfigurationProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * Writes the HTML report file to the predefined directory
 * Created by Mark Bramnik on 03/11/2016.
 */
public class DefaultTestReportProcessor implements TestReportProcessor {
    private File reportDir;
    public DefaultTestReportProcessor() {
        reportDir = new File(System.getProperty(SupportedConfigurationProperties.Client.REPORT_DIR, "."));
    }

    @Override
    public void processData(String testClass, byte[] report) {
        if(!reportDir.exists()) {
            reportDir.mkdir();
        }
        File reportFile = new File(reportDir, testClass + ".html");

        try {
            Files.write(reportFile.toPath(), report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
