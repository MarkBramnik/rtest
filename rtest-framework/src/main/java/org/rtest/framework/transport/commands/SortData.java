package org.rtest.framework.transport.commands;

import org.junit.runner.manipulation.Sorter;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class SortData extends AbstractIdSupportData {
    private String sorterClassName;

    public SortData(String id, String sorterClassName) {
        super(id);
        this.sorterClassName = sorterClassName;
    }

    // for deserialization
    public SortData() {

    }

    public Sorter getSorter()  {
        System.out.println("not implemented yet");
        return null;
    }
}
