package org.rtest.framework.server;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.*;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.rtest.exceptions.RTestException;
import org.rtest.api.annotations.RealRunner;
import org.rtest.logs.Logger;
import org.rtest.logs.LoggerFactory;
import org.spockframework.runtime.Sputnik;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Mark Bramnik on 15/09/2016.
 */
public class ServerSideInternalRemoteRunner extends Runner implements Filterable, Sortable {
    private static final Logger LOG = LoggerFactory.getLogger(ServerSideInternalRemoteRunner.class);

    private Runner nativeRunner;
    private ClassLoader remoteClassLoader;
    public ServerSideInternalRemoteRunner(ClassLoader remoteClassLoader) {
        this.remoteClassLoader = remoteClassLoader;
    }

    public void init(String className) {
        Class<?> testClass;
        try {
            testClass = remoteClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RTestException("Failed to find a class named : " + className, e);
        }
        this.nativeRunner = resolveRealRunner(testClass);

    }

    private Runner resolveRealRunner(Class<?> testClass){
        RealRunner realRunner = testClass.getAnnotation(RealRunner.class);
        if(realRunner == null) {
            // the real runner annotation is not specified - we'll just use Spock's default
            // Sputnik runner
            try {
                return new Sputnik(testClass);
            } catch (InitializationError initializationError) {
                LOG.warn("Failed to initialize a sputnik runner", initializationError);
                throw new RTestException(initializationError);
            }
        }
        else {
            try {
                return realRunner.value().getConstructor(testClass.getClass()).newInstance(testClass);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RTestException("Failed to instantiate the real runner ", e);
            }
        }
    }
    @Override
    public Description getDescription() {
        return nativeRunner.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
       nativeRunner.run(runNotifier);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        if(nativeRunner instanceof Filterable) {
            ((Filterable) nativeRunner).filter(filter);
        }
    }

    @Override
    public void sort(Sorter sorter) {
        if(nativeRunner instanceof Sortable) {
            ((Sortable) nativeRunner).sort(sorter);
        }

    }
}
