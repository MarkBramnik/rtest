package org.rtest.framework.client.dynamicreload;

import org.rtest.framework.transport.commands.RespondWithByteCodeCommand;

/**
 * Process the command of getting the resource
 * Created by Mark Bramnik on 17/11/2016.
 */
public class GettingResourceCommandProtocolProcessor {
    private ByteCodeRetriever byteCodeRetriever;
    public GettingResourceCommandProtocolProcessor(ByteCodeRetriever byteCodeRetriever) {
        this.byteCodeRetriever = byteCodeRetriever;
    }

    public RespondWithByteCodeCommand prepareLoadedResourceResponse(String id, String className) {
        byte[] byteStream = byteCodeRetriever.getByteCode(className);
        return new RespondWithByteCodeCommand(id, className, byteStream);
    }
}
