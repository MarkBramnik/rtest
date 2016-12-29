package org.rtest.webapp.testapp.sample.integration.tests

import org.rtest.spock.specification.RemoteTestSpecification
import org.rtest.spock.spring.integration.api.SpringIntegrated
import org.rtest.spock.spring.integration.context.provider.web.WebApplicationContextProvider
import org.rtest.testapp.webapp.spring.beans.Calculator
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

/**
 * The sample test that shows the spring integration technique
 * Created by Mark Bramnik on 08/11/2016.
 */


@SpringIntegrated
class SampleIntegrationTest extends RemoteTestSpecification {

    @Autowired
    Calculator calculator


    def "show the sample integration test in the real application"() {
        given: "The calculator is up and running"

        when: "I invoke the Calculator"
         def expectedResult = calculator.add(1,2)

        then: "expected result should be 3"
         expectedResult == 3
    }

    def "show another use case"() {
        given:

        when:
        def expectedResult = calculator.add(3,4)
        then:
        expectedResult == 7
    }

    @Unroll
    def "check parametrized stuff: #a and #b should be equal to #c"() {
        expect:
        c == calculator.add(a,b)
        where:
        a | b || c
        1 | 2 || 3
        0 | 0 || 0
        5 | 4 || 9
        4 | 4 || 8
    }
}
