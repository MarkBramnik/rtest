package org.rtest.framework.server.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

/**
 * Created by Mark Bramnik on 04/11/2016.
 */
public class DefaultServerReportProcessorImpl implements ServerReportProcessor {
    private File reportDir;

    public DefaultServerReportProcessorImpl() {
        this.reportDir = initiallyEvaluateReportDirectory();
    }

    @Override
    public byte[] readReport(String testClassName) {
        File reportDirectory = getReportsDirectory();
        try {
            return Files.readAllBytes(new File(reportDirectory, testClassName + ".html").toPath());

        } catch (IOException e) {
            return new byte[0]; // empty report
        }
    }

    private File initiallyEvaluateReportDirectory() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/services/com.athaydes.spockframework.report.IReportCreator.properties");
        if(is == null) {
            return fallback();
        }
        else {
            try {
                Properties props = new Properties();
                props.load(is);
                final String outputDir = props.getProperty("com.athaydes.spockframework.report.outputDir");
                if(outputDir != null) {
                    return new File(outputDir);
                }
                else {
                    return fallback();
                }

            } catch (IOException e) {
                return fallback();
            }
        }
    }
    private File fallback() {
        return new File("spock-reports");
    }
    private File getReportsDirectory() {
        // find out where spock reporting system saves its reports
        return reportDir;
    }
}
