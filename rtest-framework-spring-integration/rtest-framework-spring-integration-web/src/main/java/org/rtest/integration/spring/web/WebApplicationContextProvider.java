package org.rtest.integration.spring.web;

import org.rtest.integration.spring.api.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * This provider can be used to obtain an application context for spring web applications
 * Created by Mark Bramnik on 10/11/2016.
 */
public class WebApplicationContextProvider implements ApplicationContextProvider {
    public ApplicationContext obtainContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }
}
