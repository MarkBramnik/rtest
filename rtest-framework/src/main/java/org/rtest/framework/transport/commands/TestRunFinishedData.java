package org.rtest.framework.transport.commands;

import org.junit.runner.Result;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class TestRunFinishedData extends AbstractIdSupportData {
    private Result result;

    public TestRunFinishedData(String id, Result result) {
        super(id);
        this.result = result;
    }

    public TestRunFinishedData() {
    }

    public Result getResult() {
        return result;
    }
}
