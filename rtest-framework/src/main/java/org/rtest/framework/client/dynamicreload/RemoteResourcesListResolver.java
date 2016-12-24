package org.rtest.framework.client.dynamicreload;

import java.util.List;

/**
 * Allows to specify which resources will be asked to be brought by client
 * Created by Mark Bramnik on 22/11/2016.
 */
public interface RemoteResourcesListResolver {

    List<String> getResourcesList(Class<?> testClass);
}
