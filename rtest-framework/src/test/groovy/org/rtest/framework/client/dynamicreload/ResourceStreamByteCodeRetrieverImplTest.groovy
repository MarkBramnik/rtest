package org.rtest.framework.client.dynamicreload

import spock.lang.Specification

/**
 * Unit test
 * Created by Mark Bramnik on 26/12/2016.
 */
class ResourceStreamByteCodeRetrieverImplTest extends Specification {

    def CLASS_NAME = SampleClassToLoad.name

    def "getting the byte stream on existing class returns the valid bytecode"() {
        given: "Test and auxiliary objects are set"
        TestClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader())
        and:
        ResourceStreamByteCodeRetrieverImpl testedObject = new ResourceStreamByteCodeRetrieverImpl()
        when:"Reading an existing sample class"
        byte [] actualBytes = testedObject.getByteCode(CLASS_NAME)
        and:
        testClassLoader.setTestStream(CLASS_NAME, actualBytes)
        then: "The Byte Stream is actually loaded"
        actualBytes.size() > 0
        then: "The Loaded class is valid"
        testClassLoader.loadClass(CLASS_NAME).name == SampleClassToLoad.name
    }

    def "getting the byte stream on non existing object"() {
        given: "Test Object is set"
        ResourceStreamByteCodeRetrieverImpl testedObject = new ResourceStreamByteCodeRetrieverImpl()
        when: "Reading a non-existing class"
        byte [] actualBytes = testedObject.getByteCode("SomeNonExistingClass")
        then: "The Empty Stream is returned, nothing is loaded"
        actualBytes == ResourceStreamByteCodeRetrieverImpl.EMPTY
    }
}
