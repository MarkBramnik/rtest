package org.rtest.framework.server.dynamicloading;

import org.rtest.framework.transport.commands.RTestData;
import org.rtest.framework.transport.commands.RequestByteCodeCommand;
import org.rtest.framework.transport.commands.RespondWithByteCodeCommand;
import org.rtest.framework.transport.invoker.RemoteInvoker;

import java.util.List;

/**
 * Created by Mark Bramnik on 18/11/2016.
 */
public class RTestDynamicClassLoader extends ClassLoader {
    private RemoteInvoker remoteInvoker;
    private List<String> packagesToLoadFromClient;
    private String uuid;
    public RTestDynamicClassLoader(ClassLoader parent, RemoteInvoker remoteInvoker, String uuid, List<String> packagesToLoadFromClient) {
        super(parent);
        this.remoteInvoker = remoteInvoker;
        this.uuid = uuid;
        this.packagesToLoadFromClient = packagesToLoadFromClient;
    }

    private Class<?> tryLoadByParentClassLoader(String name, boolean resolve) {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    private boolean canAskClientToBringTheClassDefinition(String name) {
        for(String packageName : packagesToLoadFromClient) {
            if(name.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
    private byte [] bringByteStreamFromClient(String className) throws ClassNotFoundException{
        RequestByteCodeCommand requestByteCodeCommand = new RequestByteCodeCommand(uuid, className);
        remoteInvoker.send(requestByteCodeCommand);
        RTestData responseCommand = remoteInvoker.read();
        if(responseCommand instanceof RespondWithByteCodeCommand) {
            // process received byte code
            byte [] response =  ((RespondWithByteCodeCommand) responseCommand).getClassAsBytes();
            if(response.length == 0) {
                throw new ClassNotFoundException(className);
            }
            else {
                return response;
            }
        }
        else {
            throw new ClassNotFoundException(className);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cl = tryLoadByParentClassLoader(name, resolve);
        if(cl != null){
            // this class has been successfully loaded
            // by parent classloader
            return cl;
        }
        else {
            // class can be requested from client
            if(!canAskClientToBringTheClassDefinition(name)) {
                throw new ClassNotFoundException(name);
            }
            else {
                byte [] classByteStream = bringByteStreamFromClient(name);
                return defineClass(name, classByteStream, 0, classByteStream.length);
            }
        }
    }


}
