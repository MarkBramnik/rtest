package org.rtest.framework.transport.commands;

/**
 * This command will be issued by the server when a class byte code is not found on server
 * The client that runs the test will be responsible for bringing the class into server
 * Created by Mark Bramnik on 17/11/2016.
 */
public class RequestByteCodeCommand extends AbstractIdAndClassNameSupportData {
    public RequestByteCodeCommand(String id, String testClassName) {
        super(id, testClassName);
    }
}
