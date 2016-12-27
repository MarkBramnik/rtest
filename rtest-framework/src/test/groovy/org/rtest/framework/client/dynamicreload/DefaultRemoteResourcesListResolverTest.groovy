package org.rtest.framework.client.dynamicreload

import org.rtest.api.annotations.AllowedDataFetchPath
import spock.lang.Shared
import spock.lang.Specification

/**
 * Unit test
 * Created by Mark Bramnik on 27/12/2016.
 */
class DefaultRemoteResourcesListResolverTest extends Specification {

    @AllowedDataFetchPath(["a.b.c", "a.d", "abc.cde"])
    class SampleAnnotatedClass {}

    @Shared
    def testObject = new  DefaultRemoteResourcesListResolver()

    def "getting list on non annotated class results in a list of single entry - the package name"() {
        when:
        def actualList = testObject.getResourcesList(DefaultRemoteResourcesListResolver)
        then:
        actualList == [DefaultRemoteResourcesListResolver.package.name]
    }

    def "getting list on annotated class results in a list of custom paths"() {
       when:
       def actualList = testObject.getResourcesList(SampleAnnotatedClass)
       then:
       actualList == ["a.b.c", "a.d", "abc.cde"]
    }
}
