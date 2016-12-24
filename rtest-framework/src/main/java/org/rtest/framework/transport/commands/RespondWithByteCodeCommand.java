package org.rtest.framework.transport.commands;

/**
 * Created by Mark Bramnik on 17/11/2016.
 */
public class RespondWithByteCodeCommand extends AbstractIdAndClassNameSupportData {
    private byte [] classAsBytes;
    public RespondWithByteCodeCommand(String id, String testClassName, byte [] byteArray) {
        super(id, testClassName);
        this.classAsBytes = byteArray;
    }

    public byte[] getClassAsBytes() {
        return classAsBytes;
    }
}
