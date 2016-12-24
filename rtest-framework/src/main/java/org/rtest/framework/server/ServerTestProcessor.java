package org.rtest.framework.server;


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.rtest.framework.server.dynamicloading.RTestDynamicClassLoader;
import org.rtest.framework.server.reports.ServerReportProcessor;
import org.rtest.framework.transport.commands.*;
import org.rtest.framework.transport.invoker.RemoteInvoker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class ServerTestProcessor implements Runnable {
    private RemoteInvoker remoteInvoker;
    ServerReportProcessor reportProcessor;


    public ServerTestProcessor(RemoteInvoker remoteInvoker, ServerReportProcessor serverReportProcessor) {
        this.remoteInvoker = remoteInvoker;
        this.reportProcessor = serverReportProcessor;
    }

    @Override
    public void run() {
        RTestDynamicClassLoader classLoader;
        final AtomicBoolean shouldRun = new AtomicBoolean(true);
        ServerSideInternalRemoteRunner runner = null;
        while(shouldRun.get()) {
            RTestData rTestData = remoteInvoker.read();
            if(rTestData instanceof CreateRunnerData) {
                CreateRunnerData data = (CreateRunnerData)rTestData;
                // instantiate the class loader
                classLoader = new RTestDynamicClassLoader(
                        getClass().getClassLoader(),
                        remoteInvoker, data.getId(), data.getRemoteResourcesToAskFromClient());
                String classNameToLoad = data.getClassName();

                runner = new ServerSideInternalRemoteRunner(classLoader);
                runner.init(classNameToLoad);
                remoteInvoker.send(new RunnerDataCreated(data.getId(), data.getClassName()));

            }
            else if(rTestData instanceof GetDescriptionData) {
                if(runner != null) {
                    GetDescriptionData getDescriptionData = (GetDescriptionData) rTestData;
                    Description description = runner.getDescription();
                    remoteInvoker.send(new GotDescriptionData(getDescriptionData.getId(), description));
                }
            }
            else if(rTestData instanceof FilterData) {
                FilterData filterData = (FilterData) rTestData;
                if(runner != null) {
                    try {
                        runner.filter(filterData.getFilter());
                    } catch (NoTestsRemainException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(rTestData instanceof SortData) {
                SortData sortedData = (SortData) rTestData;
                if(runner != null) {
                   runner.sort(sortedData.getSorter());
                }
            }
            else if(rTestData instanceof StartRunningData) {
                if(runner != null) {
                    StartRunningData startRunningData = (StartRunningData) rTestData;
                    RunNotifier runNotifier = new RunNotifier();
                    runNotifier.addListener(new RemoteRunNotificationListener(remoteInvoker, startRunningData.getId()));

                    Result result = new Result();
                    RunListener listener= result.createListener();
                    runNotifier.addFirstListener(listener);
                    runNotifier.fireTestRunStarted(runner.getDescription());
                    runner.run(runNotifier);
                    runNotifier.fireTestRunFinished(result);
                }
            }
            else if(rTestData instanceof AskForReportData) {
                // read generated report and send the report back
                AskForReportData askForReportData = (AskForReportData) rTestData;
                byte [] reportBytes = reportProcessor.readReport(askForReportData.getClassName());
                TestReportGeneratedData reportGeneratedData = new TestReportGeneratedData(askForReportData.getId(), askForReportData.getClassName(), reportBytes);
                remoteInvoker.send(reportGeneratedData);
            }
            else if(rTestData instanceof EndProtocolData) {
                shouldRun.set( false );
            }
        }
        remoteInvoker.close();




    }
}
