package org.rtest.framework.client.dynamicreload;

/**
 * Establishes a contract of getting the class byte code by name
 * The class shouldn't be loaded into memory during the execution of the interface's method
 * Created by Mark Bramnik on 17/11/2016.
 */
public interface ByteCodeRetriever {

    byte [] getByteCode(String className);
}
