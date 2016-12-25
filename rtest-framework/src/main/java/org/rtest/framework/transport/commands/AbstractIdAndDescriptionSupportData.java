package org.rtest.framework.transport.commands;

import org.junit.runner.Description;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public abstract class AbstractIdAndDescriptionSupportData extends AbstractIdSupportData {
    private Description description;

    public AbstractIdAndDescriptionSupportData(String id, Description description) {
        super(id);
        this.description = description;
    }

    public Description getDescription() {
        return description;
    }
}
