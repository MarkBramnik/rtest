package org.rtest.spock.local

import spock.lang.Specification

/**
 * Created by Mark Bramnik on 02/11/2016.
 */
class AnotherLocalSpockSpecification extends Specification {
    def "sample test 1"() {
        given:
          def a = 3
          def b = 4
        when:
          def c = a - b
        then:
          c == -1

    }
}
