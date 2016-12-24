package org.rtest.framework.client.dynamicreload;

import org.rtest.api.annotations.AllowedDataFetchPath;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * By default checks whether the custom path has been specified by annotation and if not, just allows to bring everything in the package
 * Otherwise allows to bring exactly what has been specified at the level of packages or classes
 * Created by Mark Bramnik on 22/11/2016.
 */
public class DefaultRemoteResourcesListResolver implements RemoteResourcesListResolver {
    @Override
    public List<String> getResourcesList(Class<?> testClass) {
        if(testClass.isAnnotationPresent(AllowedDataFetchPath.class)) {
            AllowedDataFetchPath customPaths = testClass.getAnnotation(AllowedDataFetchPath.class);
            String [] values = customPaths.value();
            return Arrays.asList(values);
        }
        else {
            return Collections.singletonList(testClass.getPackage().getName());
        }

    }
}
