package org.rtest.framework.client.dynamicreload;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark Bramnik on 26/12/2016.
 */
public class TestClassLoader extends ClassLoader {
    private Map<String, byte []> testStreams = new HashMap<>();

    public TestClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void setTestStream(String name, byte [] testStream) {
        this.testStreams.put(name, testStream);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(testStreams.containsKey(name)) {
            return defineClass(name, testStreams.get(name), 0, testStreams.get(name).length );
        }
        else {
            return super.loadClass(name);
        }

    }
}
