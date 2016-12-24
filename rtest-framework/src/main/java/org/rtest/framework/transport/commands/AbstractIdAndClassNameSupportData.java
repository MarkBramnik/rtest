package org.rtest.framework.transport.commands;

import org.rtest.exceptions.RTestException;

/**
 * Created by Mark Bramnik on 03/11/2016.
 */
public abstract class AbstractIdAndClassNameSupportData extends AbstractIdSupportData {
    private String testClassName;

    public AbstractIdAndClassNameSupportData(String id, String testClassName) {
        super(id);
        this.testClassName = testClassName;
    }

    public String getClassName() {
        return testClassName;
    }
    public Class<?> getClassNameAsClass() {
        try {
            return Class.forName(testClassName);
        } catch (ClassNotFoundException e) {
            throw new RTestException("Failed to create test class", e);
        }
    }
}
