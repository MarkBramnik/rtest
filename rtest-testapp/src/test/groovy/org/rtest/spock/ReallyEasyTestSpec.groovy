package org.rtest.spock


import spock.lang.Narrative
import spock.lang.Unroll

/**
 * Created by Mark Bramnik on 19/09/2016.
 */

@Narrative("Running a really easy specification here")
class ReallyEasyTestSpec
        extends AbstractMyAppSpec {


    def setupSpec() {
        println 'called setup spec'
    }
    def setup() {
        println 'called setup'
    }
    def cleanup() {
        println 'called cleanup'
    }
    def cleanupSpec() {
        println 'called cleanup spec'
    }

    def "check that very simple test works"() {
        expect:
        3 == 1 + 2
    }
    def "check that another simple test works"() {
        given: "the first two numbers are specified"
          def a = 1
          def b = 2

        when: "I add the first two numbers"
          def c = a + b
         and: "I substract the numbers"
          def d = a - b
        then: "the sum is calculated correctly"
          c == 3
        then: "the difference is calculated correctly"
          d == -1
    }

    @Unroll
    def "sample parametrized test #a + #b should be equal to #c"() {
        expect: "sum of two numbers should be calculated correctly"
          a + b  == c
        where:
          a |  b || c
          1 |  2 || 3
          2 |  4 || 6
          3 | -1 || 2
    }

    @Unroll
    def "another parametrized test here #a eq #b"() {
        expect:
          a == b
        where:
          a  |  b
          2  |  2
          3  |  3



    }

}
