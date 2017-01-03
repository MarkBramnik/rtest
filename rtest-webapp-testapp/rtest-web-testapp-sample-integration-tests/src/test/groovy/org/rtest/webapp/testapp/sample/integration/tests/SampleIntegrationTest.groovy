package org.rtest.webapp.testapp.sample.integration.tests
import org.rtest.integration.spring.api.*
import org.rtest.integration.spring.web.WebApplicationContextProvider;
import org.rtest.spock.specification.RemoteTestSpecification
import org.rtest.testapp.webapp.spring.beans.Calculator
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

/**
 * The sample test that shows the spring integration technique
 * Created by Mark Bramnik on 08/11/2016.
 */


@SpringIntegrated(WebApplicationContextProvider)
class SampleIntegrationTest extends RemoteTestSpecification {

    @Autowired
    Calculator calculator

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


}
