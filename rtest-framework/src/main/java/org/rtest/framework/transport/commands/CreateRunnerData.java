package org.rtest.framework.transport.commands;

import java.util.List;

/**
 * Created by Mark Bramnik on 18/09/2016.
 */
public class CreateRunnerData extends AbstractIdAndClassNameSupportData {

    private List<String> remoteResourcesToAskFromClient;
    public CreateRunnerData(String id, String testClassName, List<String> remoteResourcesToAskFromClient) {
        super(id, testClassName);
        this.remoteResourcesToAskFromClient = remoteResourcesToAskFromClient;
    }

    public List<String> getRemoteResourcesToAskFromClient() {
        return remoteResourcesToAskFromClient;
    }
}
