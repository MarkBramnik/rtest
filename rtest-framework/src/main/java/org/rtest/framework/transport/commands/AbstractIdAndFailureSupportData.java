package org.rtest.framework.transport.commands;

import org.junit.runner.notification.Failure;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public abstract class AbstractIdAndFailureSupportData extends AbstractIdSupportData {
    private Failure failure;

    public AbstractIdAndFailureSupportData(String id, Failure failure) {
        super(id);
        this.failure = failure;
    }

    public AbstractIdAndFailureSupportData() {

    }

    public Failure getFailure() {
        return failure;
    }
}
