package org.rtest.api.annotations;

import java.lang.annotation.*;

/**
 * Specifies which data paths can be asked to be brought from client
 * Created by Mark Bramnik on 22/11/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface AllowedDataFetchPath {
    String [] value();
}
