package org.rtest.framework.client.dynamicreload

import spock.lang.Specification

/**
 * Unit Test
 * Created by Mark Bramnik on 27/12/2016.
 */
class GettingResourceCommandProtocolProcessorTest extends Specification {

   def SAMPLE_CLASS_NAME = "sampleClassName"
   def SAMPLE_BYTE_STREAM = [0,1,2,3] as byte []
   def SAMPLE_ID = "sampleId"
   def "the processor retrieves the resource and produces the correct command"() {
       given:
       def mockByteCodeRetriever = Mock(ByteCodeRetriever)
       1 * mockByteCodeRetriever.getByteCode("sampleClassName") >> SAMPLE_BYTE_STREAM

       def testObject = new GettingResourceCommandProtocolProcessor(mockByteCodeRetriever)
       when:
       def actualCommand = testObject.prepareLoadedResourceResponse(SAMPLE_ID, SAMPLE_CLASS_NAME)
       then:
       actualCommand.id           == SAMPLE_ID
       actualCommand.classAsBytes == SAMPLE_BYTE_STREAM
       actualCommand.className    == SAMPLE_CLASS_NAME

   }
}
