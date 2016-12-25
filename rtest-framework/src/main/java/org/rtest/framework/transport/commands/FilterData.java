package org.rtest.framework.transport.commands;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;

import java.util.Map;

/**
 * Created by Mark Bramnik on 14/09/2016.
 */
public class FilterData extends AbstractIdSupportData {
    private Map<Description, Boolean> context;
    public FilterData(String id, Map<Description, Boolean> context) {
        super(id);
        this.context = context;
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            public boolean shouldRun(Description description) {
                return Boolean.TRUE.equals(context.get(description));
            }

            @Override
            public String describe() {
                return "RTest Filter";
            }

            @Override
            public void apply(Object child) throws NoTestsRemainException {
                if(child instanceof Filterable) {
                    Filterable filterableChild = (Filterable) child;
                    filterableChild.filter(this);
                }
            }
        };
        //return Filter.matchMethodDescription(desiredDescription);
           /*return new Filter() {
               @Override
               public boolean shouldRun(Description description) {
                   return (toRun.contains(description));

               }

               @Override
               public String describe() {
                   return "RTest methods filter";
               }
           };*/

    }
}
