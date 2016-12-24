package org.rtest.framework.server;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.rtest.framework.transport.commands.*;
import org.rtest.framework.transport.invoker.RemoteInvoker;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class RemoteRunNotificationListener extends RunListener {
    private RemoteInvoker remoteInvoker;
    private String uuid;
    private ProtocolConverter protocolConverter = new ProtocolConverter();
    public RemoteRunNotificationListener(RemoteInvoker remoteInvoker, String uuid) {
        this.remoteInvoker = remoteInvoker;
        this.uuid = uuid;
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        remoteInvoker.send(new TestRunStartedData(uuid, description));
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        Result convertedResult = protocolConverter.convertResult(result);
        remoteInvoker.send(new TestRunFinishedData(uuid, convertedResult));
    }

    @Override
    public void testStarted(Description description) throws Exception {
        remoteInvoker.send(new TestStartedData(uuid, description));
    }

    @Override
    public void testFinished(Description description) throws Exception {
        remoteInvoker.send(new TestFinishedData(uuid, description));
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        Failure currentFailure = protocolConverter.convertFailure(failure);
        remoteInvoker.send(new TestFailureData(uuid, currentFailure));
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        Failure currentFailure = protocolConverter.convertFailure(failure);
        remoteInvoker.send(new TestAssumptionFailedData(uuid, currentFailure));
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        remoteInvoker.send(new TestIgnoredData(uuid, description));
    }
}
