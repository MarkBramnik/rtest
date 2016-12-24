package org.rtest.spock

import spock.lang.Unroll

class SampleRemoteSpecification extends AbstractMyAppSpec {

    @Unroll
    def "check that max of #a and #b always gives #c"() {
        
        expect:
            c == Math.max( a, b)
        where:
            a | b | c
            4 | 5 | 5
            2 | 1 | 2

    }


    def "check remote speck"() {
        expect:
            "abc" == "cba".reverse()

    }

    @Unroll
    def "another test specification: #a should be a sum of #b and #c"() {

        expect:
            a == b + c
        where:
            a |  b | c
            5 |  3 | 2
            10|  4 | 6
            2 |  1 | 1
    }

}