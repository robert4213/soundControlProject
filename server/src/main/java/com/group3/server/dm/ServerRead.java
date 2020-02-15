package com.group3.server.dm;

import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.registration.Registration;

/**
 * Created by Wing Yu on 11/4/2019 2:12 PM).
 * Blog: http://www.wingyu.org/
 * GitHub: https://github.com/wing324
 * Email: wing.yumin@gmail.com
 */
public class ServerRead {

    public void read(LeshanServer server, Registration registration, int objectId, int objectInstanceId, int resourceId) throws InterruptedException {
        // Read Client Resource
        ReadRequest readRequest = new ReadRequest(objectId,objectInstanceId,resourceId);
        ReadResponse readResponse = server.send(registration, readRequest);
        System.out.println("========Read Resource /"+objectId+ "/"+objectInstanceId+"/" +resourceId+"======");
        if(readResponse.isSuccess()){
            System.out.println(readResponse.toString());
            System.out.println("/"+objectId+"/"+objectInstanceId+"/"+resourceId+" Value: " + ((LwM2mResource)readResponse.getContent()).getValue());
        } else {
            System.out.println("Failed  to read: " + readResponse.getCode() + " " + readResponse.getErrorMessage());
        }
    }

    public int readValue(LeshanServer server, Registration registration, int objectId, int objectInstanceId, int resourceId) throws InterruptedException {
        // Read Client Resource
        ReadRequest readRequest = new ReadRequest(objectId,objectInstanceId,resourceId);
        ReadResponse readResponse = server.send(registration, readRequest);
        return Integer.parseInt(String.valueOf(((LwM2mResource)readResponse.getContent()).getValue()));
    }

    public String readStringValue(LeshanServer server, Registration registration, int objectId, int objectInstanceId, int resourceId) throws InterruptedException {
        // Read Client Resource
        ReadRequest readRequest = new ReadRequest(objectId,objectInstanceId,resourceId);
        ReadResponse readResponse = server.send(registration, readRequest);
        return String.valueOf(((LwM2mResource)readResponse.getContent()).getValue());
    }

}
