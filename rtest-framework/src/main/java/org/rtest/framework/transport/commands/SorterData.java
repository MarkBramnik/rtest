package org.rtest.framework.transport.commands;

/**
 * Created by Mark Bramnik on 14/09/2016.
 */
public class SorterData extends AbstractIdSupportData {
    private String sorterClassName;

    public SorterData(String id, String sorterClassName) {
        super(id);
        this.sorterClassName = sorterClassName;
    }

    public String getSorter() {
        return sorterClassName;
    }
}
