package org.rtest.api.annotations;

import org.junit.runner.Runner;

import java.lang.annotation.*;

/**
 * Created by Mark Bramnik on 06/09/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited

public @interface RealRunner {
    Class<? extends Runner> value();
}
