package org.rtest.framework.transport.commands;

import org.junit.runner.Description;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class TestRunStartedData extends AbstractIdAndDescriptionSupportData {
    public TestRunStartedData(String id, Description description) {
        super(id, description);
    }
}
