package org.rtest.spock.spring.integration.api;

import org.springframework.context.ApplicationContext;

/**
 * Responsible
 * Created by Mark Bramnik on 10/11/2016.
 */
public interface ApplicationContextProvider {
    ApplicationContext obtainContext();
}
