package org.rtest.integration.spring.impl;

import org.rtest.exceptions.RTestException;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FieldInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Will be executed by spock before every test method
 * Created by Mark Bramnik on 12/11/2016.
 */

public class SpringInjectionInterceptor implements IMethodInterceptor {
    private ApplicationContext applicationContext;
    private List<FieldInfo> fields;

    public SpringInjectionInterceptor(ApplicationContext applicationContext, List<FieldInfo> fields) {
        this.applicationContext = applicationContext;
        this.fields = findFieldsRelevantForSpringInjection(fields);
    }
    private List<FieldInfo> findFieldsRelevantForSpringInjection(List<FieldInfo> allFields) {
        List<FieldInfo> result = new ArrayList<>();
        for(FieldInfo fi : allFields) {
            if(fi.getReflection().isAnnotationPresent(Autowired.class)) {
                result.add(fi);
            }
        }
        return result;
    }

    @Override
    public void intercept(IMethodInvocation invocation) throws Throwable {
        autowireFields(invocation.getInstance());
        invocation.proceed();
    }

    private void autowireFields(Object instance) {
        for(FieldInfo fieldInfo : fields) {
            autowireField(fieldInfo, instance);
        }

    }
    private void autowireField(FieldInfo fieldInfo, Object instance) {
        //Autowired autowired = fieldInfo.getReflection().getAnnotation(Autowired)
        // try to get bean by name
        Object fieldValue =  obtainValueFromSpring(fieldInfo);
        fieldInfo.writeValue(instance, fieldValue);
    }

    private Object obtainValueFromSpring(FieldInfo fieldInfo) {
        Object beanByName = applicationContext.getBean(fieldInfo.getName());
        if (beanByName == null) {
            // failed to find bean by name, try to resolve by type
            Map map = applicationContext.getBeansOfType(fieldInfo.getType());
            if (map.size() > 1) {
                throw new RTestException("Too many beans - could not autowire: " + fieldInfo);
            } else {
                return map.values().iterator().next();
            }

        } else if (fieldInfo.getType().isAssignableFrom(beanByName.getClass())) {
            return beanByName;
        } else throw new RTestException("Failed to autowire field: " + fieldInfo);
    }
}
