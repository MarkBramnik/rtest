package org.rtest.spock.spring.integration.api;

import org.rtest.spock.spring.integration.context.provider.web.WebApplicationContextProvider;
import org.rtest.spock.spring.integration.impl.RemoteSpringAnnotationDrivenExtension;
import org.spockframework.runtime.extension.ExtensionAnnotation;

import java.lang.annotation.*;

/**
 * This annotation should be put on specification.
 * It will denote spring integration availability
 *
 * Created by Mark Bramnik on 10/11/2016.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@ExtensionAnnotation(RemoteSpringAnnotationDrivenExtension.class)
@Documented
public @interface SpringIntegrated {
    Class<? extends ApplicationContextProvider> value() default WebApplicationContextProvider.class;
}
