package org.rtest.framework.client.dynamicreload;

import org.rtest.logs.Logger;
import org.rtest.logs.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads the bytecode of the class from the classpath. Assumes the the requested resource
 * has to be found there
 * Created by Mark Bramnik on 17/11/2016.
 */
public class ResourceStreamByteCodeRetrieverImpl implements ByteCodeRetriever {
    final static byte [] EMPTY = new byte[0];

    private static final Logger LOG = LoggerFactory.getLogger(ResourceStreamByteCodeRetrieverImpl.class);

    public byte [] getByteCode(String className) {
        String resourceName = "/" + className.replace(".", "/") + ".class";
        InputStream is = getClass().getResourceAsStream(resourceName);
        if(is == null) {
            return EMPTY;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int currByte;
        try {
            while ((currByte = is.read()) != -1) {
                buffer.write(currByte);
            }
            buffer.flush();
        }catch (IOException ex) {
            LOG.error("Cannot read the source: " + className + " is it in classPath?");
            // can't read the resource
            return EMPTY;
        }
        byte [] result = buffer.toByteArray();
        try {
            buffer.close();
        } catch (IOException e) {
            LOG.error("Failed to read source file: " + className, e);
            return result;
        }
        try {
            is.close();
        }catch (IOException ex) {
            return result;
        }
        return result;

    }
}
