package org.rtest.framework.transport.commands;

import org.junit.runner.notification.Failure;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class TestAssumptionFailedData extends AbstractIdAndFailureSupportData {
    public TestAssumptionFailedData(String id, Failure failure) {
        super(id, failure);
    }

}
