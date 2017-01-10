package org.rtest.framework.client

import org.rtest.exceptions.RTestException
import spock.lang.Specification

/**
 * Unit Test
 * Created by Mark Bramnik on 09/01/2017.
 */
class ClientSocketSupplierTest extends Specification {
    def "happy path: socket supplier creates the socket"() {
        given:
           def host = "localhost"
           def port = 1234
           def testObject = new ClientSocketSupplier(host, port)
        when:
            testObject.getSocket()
        then:
           def e = thrown(RTestException)
        then:
           e.message == "Failed to create socket for [ host : $host , port : $port ]"
        then:
           e.cause.class == ConnectException    
    }
}
