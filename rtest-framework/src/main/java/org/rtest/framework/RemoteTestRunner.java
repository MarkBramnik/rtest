package org.rtest.framework;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.*;
import org.junit.runner.notification.RunNotifier;
import org.rtest.framework.config.props.SupportedConfigurationProperties;

/**
 * An implementation of the test runner.
 * <p/>
 * Must be specified on the tests with {@link org.junit.runner.RunWith} annotation
 * Causes the methods of the test to be processed on remote server
 * Created by Mark Bramnik on 13/09/2016.
 */
public class RemoteTestRunner extends Runner implements Filterable, Sortable {
    /* we still maintain the abstraction and not supply the client implementation in this class only because
     * maybe in future we'll have to create an instance of this class on server as well
     * After all, the @RunWith(RemoteTestRunner) will exist on server as well, so that class gets loaded into the server memory,
     * although not constructed
     */
    private Runner realRunner;
    private RealRunnerProvider realRunnerProvider = new RealRunnerProviderImpl();


    public RemoteTestRunner(Class<?> testClass) {
        realRunner = realRunnerProvider.getClientRunner(testClass);
    }

    @Override
    public Description getDescription() {
       return realRunner.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        realRunner.run(runNotifier);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        if(realRunner instanceof Filterable) {
            ((Filterable)realRunner).filter(filter);
        }
    }

    @Override
    public void sort(Sorter sorter) {
        if(realRunner instanceof Sortable) {
            ((Sortable)realRunner).sort(sorter);
        }

    }


}
