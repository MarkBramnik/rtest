package org.rtest.framework.client

import org.rtest.exceptions.RTestException
import org.rtest.framework.common.utils.Clock
import org.rtest.framework.common.utils.ThreadSleeper
import org.rtest.framework.transport.invoker.SocketSupplier
import spock.lang.Specification

/**
 * Unit Test
 * Created by Mark Bramnik on 09/01/2017.
 */
class RetrySupportClientSocketSupplierTest extends Specification {
    def "happy path: when socket is created successfully no exception is thrown"() {
        given:
            def socketSupplier    = Mock(SocketSupplier)
            def maxWaitTimePeriod = 0l
            def clock             = Mock(Clock)
            def threadSleeper     = Mock(ThreadSleeper)
            def expectedSocket    = Mock(Socket)
            RetrySupportClientSocketSupplier testedObject = new RetrySupportClientSocketSupplier(
                    socketSupplier,
                    maxWaitTimePeriod,
                    clock,
                    threadSleeper
            )
        when:
            Socket actualSocket = testedObject.getSocket()
        then:
            1 * socketSupplier.getSocket() >> expectedSocket
            actualSocket == expectedSocket
        then:
            notThrown(RTestException)
    }

    def "when socket is created at the second attempt the test will wait for one second"() {
        given:
            def socketSupplier    = Mock(SocketSupplier)
            def maxWaitTimePeriod = 1000l
            def clock             = Mock(Clock)
            def threadSleeper     = Mock(ThreadSleeper)
            def expectedSocket    = Mock(Socket)
            RetrySupportClientSocketSupplier testedObject = new RetrySupportClientSocketSupplier(
                    socketSupplier,
                    maxWaitTimePeriod,
                    clock,
                    threadSleeper
            )
        when:
            Socket actualSocket = testedObject.getSocket()
        then:
            1 * socketSupplier.getSocket() >> { throw new RTestException(new ConnectException()) }
            1 * socketSupplier.getSocket() >> expectedSocket
            _ * clock.currentTime >> 0l

            1 * threadSleeper.sleep( {it == 1000l} ) >> _
            actualSocket == expectedSocket
        then:
            notThrown(RTestException)
    }


}
