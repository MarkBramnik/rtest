package org.rtest.framework.client;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.*;
import org.junit.runner.notification.RunNotifier;
import org.rtest.framework.client.dynamicreload.DefaultRemoteResourcesListResolver;
import org.rtest.framework.client.dynamicreload.GettingResourceCommandProtocolProcessor;
import org.rtest.framework.client.dynamicreload.RemoteResourcesListResolver;
import org.rtest.framework.client.dynamicreload.ResourceStreamByteCodeRetrieverImpl;
import org.rtest.framework.client.reports.DefaultTestReportProcessor;
import org.rtest.framework.client.reports.TestReportProcessor;
import org.rtest.framework.config.props.SupportedConfigurationProperties;
import org.rtest.framework.transport.commands.*;
import org.rtest.framework.transport.invoker.RemoteInvoker;
import org.rtest.logs.Logger;
import org.rtest.logs.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Implements the running protocol on the client side
 * Creates commands to the server and responds to them
 * Since its still implemented as a runner it adheres to the lifecycle of JUnit Runners
 * Created by Mark Bramnik on 14/09/2016.
 */
public class ClientSideInternalRemoteRunner extends Runner implements Filterable, Sortable{
    private Class<?> testClass;
    private RemoteInvoker remoteInvoker;
    private String uuid;
    private FilterDataCreator filterDataCreator = new FilterDataCreator();
    private TestReportProcessor testReportProcessor = new DefaultTestReportProcessor();
    private GettingResourceCommandProtocolProcessor getResourceProcessor = new GettingResourceCommandProtocolProcessor(new ResourceStreamByteCodeRetrieverImpl());
    private RemoteResourcesListResolver remoteResourcesListResolver = new DefaultRemoteResourcesListResolver();
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSideInternalRemoteRunner.class);
    // if this value is set to false, the client side won't ask the server to bring reports of the test
    private boolean askServerForReports = Boolean.valueOf(System.getProperty(SupportedConfigurationProperties.Client.ASK_SERVER_FOR_REPORTS, "true"));

    public ClientSideInternalRemoteRunner(Class<?> testClass, RemoteInvoker remoteInvoker) {
        this.testClass = testClass;
        this.remoteInvoker = remoteInvoker;
        this.uuid = UUID.randomUUID().toString();

    }

    public void init() {
        List<String> remoteResourceList = remoteResourcesListResolver.getResourcesList(testClass);
        remoteInvoker.send(new CreateRunnerData(uuid, testClass.getName(), remoteResourceList));
        boolean fullyInitialized = false;
        while(!fullyInitialized) {
            RTestData cmd = remoteInvoker.read();
            if (cmd instanceof RunnerDataCreated) {
                RunnerDataCreated runnerDataCreated = (RunnerDataCreated) cmd;
                LOGGER.debug("Created runner data for " + runnerDataCreated.getClassName());
                fullyInitialized = true;
            }
            else if(cmd instanceof RequestByteCodeCommand) {
                RequestByteCodeCommand requestByteCodeCommand = (RequestByteCodeCommand) cmd;
                RespondWithByteCodeCommand respondWithByteCodeCommand = getResourceProcessor.prepareLoadedResourceResponse(uuid, requestByteCodeCommand.getClassName());
                remoteInvoker.send(respondWithByteCodeCommand);
            }
        }
    }



    @Override
    public Description getDescription() {
        GetDescriptionData getDescription = new GetDescriptionData(uuid);
        remoteInvoker.send(getDescription);
        GotDescriptionData gotDescriptionData = (GotDescriptionData) remoteInvoker.read();
        return gotDescriptionData.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        StartRunningData startRunningData = new StartRunningData(uuid);
        remoteInvoker.send(startRunningData);
        boolean shouldProceed = true;
        while(shouldProceed) {
            RTestData rTestData = remoteInvoker.read();
            if(rTestData instanceof TestRunStartedData) {
                TestRunStartedData data = (TestRunStartedData) rTestData;
                runNotifier.fireTestRunStarted(data.getDescription());
            } else if(rTestData instanceof TestStartedData) {
                TestStartedData data = (TestStartedData) rTestData;
                runNotifier.fireTestStarted(data.getDescription());
            } else if(rTestData instanceof TestFinishedData) {
                TestFinishedData data = (TestFinishedData) rTestData;
                runNotifier.fireTestFinished(data.getDescription());
            } else if(rTestData instanceof TestIgnoredData) {
                TestIgnoredData data = (TestIgnoredData) rTestData;
                runNotifier.fireTestIgnored(data.getDescription());
            } else if(rTestData instanceof TestFailureData) {
                TestFailureData data = (TestFailureData) rTestData;
                runNotifier.fireTestFailure(data.getFailure());
            } else if(rTestData instanceof TestAssumptionFailedData) {
                TestAssumptionFailedData data = (TestAssumptionFailedData) rTestData;
                runNotifier.fireTestAssumptionFailed(data.getFailure());
            } else if(rTestData instanceof TestRunFinishedData) {
                TestRunFinishedData data = (TestRunFinishedData) rTestData;
                runNotifier.fireTestRunFinished(data.getResult());
                if(!askServerForReports) {
                    remoteInvoker.send(new EndProtocolData(uuid, testClass.getName()));
                    shouldProceed = false;
                }
                else {
                    AskForReportData askForReportDataCommand = new AskForReportData(uuid, testClass.getName());
                    remoteInvoker.send(askForReportDataCommand);
                }
            } else if(rTestData instanceof TestReportGeneratedData) {
                TestReportGeneratedData testReportGeneratedData = (TestReportGeneratedData) rTestData;
                testReportProcessor.processData(testReportGeneratedData.getTestClass(), testReportGeneratedData.getReport());
                remoteInvoker.send(new EndProtocolData(uuid, testClass.getName()));
                shouldProceed = false;
            } else if(rTestData instanceof RequestByteCodeCommand) {
                RequestByteCodeCommand requestByteCodeCommand = (RequestByteCodeCommand) rTestData;
                LOGGER.debug("Getting information about class: " + requestByteCodeCommand.getClassName());
                RespondWithByteCodeCommand respondWithByteCodeCommand = getResourceProcessor.prepareLoadedResourceResponse(uuid, requestByteCodeCommand.getClassName());
                remoteInvoker.send(respondWithByteCodeCommand);
            }


        }
        remoteInvoker.close();

    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        // we can't serialize a filter here, so we'll create a new filter data
        // that will contain only children which really should run

        FilterData filterData = filterDataCreator.createFilterData(uuid, filter, getDescription());
        remoteInvoker.send(filterData);

    }
    @Override
    public void sort(Sorter sorter) {
        RTestData sortData = new SortData(sorter.toString(), uuid);
        remoteInvoker.send(sortData);

    }
}
