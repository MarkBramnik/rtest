package org.rtest.framework.transport.commands;

/**
 * Created by Mark Bramnik on 15/09/2016.
 */
public class AbstractIdSupportData implements RTestData {

    private String id;

    public AbstractIdSupportData(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
