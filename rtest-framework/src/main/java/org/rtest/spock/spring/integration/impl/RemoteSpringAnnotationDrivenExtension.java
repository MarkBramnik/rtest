package org.rtest.spock.spring.integration.impl;

import org.rtest.exceptions.RTestException;
import org.rtest.spock.spring.integration.api.ApplicationContextProvider;
import org.rtest.spock.spring.integration.api.SpringIntegrated;
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FieldInfo;
import org.spockframework.runtime.model.SpecInfo;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * An annotation extension for spock to implement spring di integration
 * Created by Mark Bramnik on 21/11/2016.
 */
public class RemoteSpringAnnotationDrivenExtension  extends AbstractAnnotationDrivenExtension<SpringIntegrated> {
    private interface Filter<T> {
        boolean accept(T t);
    }
    private List<FieldInfo> findByFilter(List<FieldInfo> list, Filter<FieldInfo> filter) {
        List<FieldInfo> result = new ArrayList<>();
        for(FieldInfo fi : list) {
            if(filter.accept(fi)) {
                result.add(fi);
            }
        }
        return result;
    }
    private List<FieldInfo> findAllSharedFields(List<FieldInfo> allFields){
        return findByFilter(allFields, new Filter<FieldInfo>() {
            @Override
            public boolean accept(FieldInfo fieldInfo) {
                return fieldInfo.isShared();
            }
        });
    }
    private List<FieldInfo> findAllNonSharedFields(List<FieldInfo> allFields) {
        return findByFilter(allFields, new Filter<FieldInfo>() {
            @Override
            public boolean accept(FieldInfo fieldInfo) {
                return !fieldInfo.isShared();
            }
        });    }
    @Override
    public void visitSpecAnnotation(SpringIntegrated annotation, SpecInfo spec) {
        ApplicationContext applicationContext = getApplicationContext(annotation);
        // spec.getAllFields brings also super class's (specification) fields
        List<FieldInfo> allFields = spec.getAllFields();
        List<FieldInfo> sharedFields    = findAllSharedFields(allFields);
        List<FieldInfo> nonSharedFields = findAllNonSharedFields(allFields);
        spec.addSetupInterceptor(new SpringInjectionInterceptor(applicationContext, nonSharedFields ));
        spec.addSetupSpecInterceptor(new SpringInjectionInterceptor(applicationContext, sharedFields ));
    }

    private ApplicationContext getApplicationContext(SpringIntegrated annotation) {
        ApplicationContextProvider appContextProvider;
        try {
            appContextProvider = annotation.value().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RTestException("Failed to obtain spring application context", e);
        }
        return appContextProvider.obtainContext();
    }
}
