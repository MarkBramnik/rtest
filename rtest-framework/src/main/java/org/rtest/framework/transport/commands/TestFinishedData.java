package org.rtest.framework.transport.commands;

import org.junit.runner.Description;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class TestFinishedData  extends AbstractIdAndDescriptionSupportData {
    public TestFinishedData(String id, Description description) {
        super(id, description);
    }
}
