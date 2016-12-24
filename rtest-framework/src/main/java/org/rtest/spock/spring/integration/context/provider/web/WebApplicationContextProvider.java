package org.rtest.spock.spring.integration.context.provider.web;

import org.rtest.spock.spring.integration.api.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Created by Mark Bramnik on 10/11/2016.
 */
public class WebApplicationContextProvider implements ApplicationContextProvider {
    public ApplicationContext obtainContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }
}
