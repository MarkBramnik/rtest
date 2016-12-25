package org.rtest.framework.transport.commands;

import org.junit.runner.Description;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class TestStartedData  extends AbstractIdAndDescriptionSupportData {
    public TestStartedData(String id, Description description) {
        super(id, description);
    }

}
