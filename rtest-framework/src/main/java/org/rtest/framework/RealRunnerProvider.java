package org.rtest.framework;

import org.junit.runner.Runner;

/**
 * Created by Mark Bramnik on 14/09/2016.
 */
public interface RealRunnerProvider {

    Runner getClientRunner(Class<?> testClass);

}
