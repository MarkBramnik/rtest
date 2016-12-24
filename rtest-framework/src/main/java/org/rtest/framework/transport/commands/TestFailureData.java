package org.rtest.framework.transport.commands;

import org.junit.runner.notification.Failure;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class TestFailureData extends AbstractIdAndFailureSupportData {

    public TestFailureData(String id, Failure failure) {
        super(id, failure);
    }

    public TestFailureData() {
    }
}
