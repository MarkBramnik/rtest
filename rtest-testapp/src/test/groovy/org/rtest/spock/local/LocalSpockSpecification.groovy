package org.rtest.spock.local

import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Mark Bramnik on 21/09/2016.
 */
@Narrative("""
Various tests to run
Spock on a local environment without my framework
""")
class LocalSpockSpecification extends Specification {

    def "test the sum of two numbers"() {
        given: "two numbers are set"
          def a = 2
          def b = 4
        when: "I add two numbers"
          def c = a + b
        then: "I expect to see the sum calculated as expected"
          c == 6
        and : "The sum should be 6"
          c == 6
    }

    @Unroll
    def "sum of #a and #b should be #c"() {
        expect:
          a + b == c
        where:
          a | b || c
          3 | 5 || 8
          0 | 0 || 0
          -1| 1 || 0
          5 | 4 || 10

    }
}
