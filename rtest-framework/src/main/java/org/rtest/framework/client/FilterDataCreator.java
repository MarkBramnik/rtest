package org.rtest.framework.client;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.rtest.framework.transport.commands.FilterData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark Bramnik on 20/09/2016.
 */
public class FilterDataCreator {
    public FilterData createFilterData(String id, Filter filter, Description desc) {
       Map<Description, Boolean> accumulator = new HashMap<>();
       populateDescriptionRunningMap(desc, accumulator, filter);
       return new FilterData(id, accumulator);
    }
    private void populateDescriptionRunningMap(Description description, Map<Description, Boolean> accumulator, Filter filter) {
        if(description.isTest()) {
            Boolean shouldRunStatus = filter.shouldRun(description);
            accumulator.put(description, shouldRunStatus);
            return;
        }
        List<Description> children = description.getChildren();
        for(Description d : children) {
            populateDescriptionRunningMap(d, accumulator, filter);
        }
    }
}
