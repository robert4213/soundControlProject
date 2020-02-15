package com.group3.server.dm;

import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.registration.Registration;

/**
 * Created by Wing Yu on 11/4/2019 2:39 PM).
 * Blog: http://www.wingyu.org/
 * GitHub: https://github.com/wing324
 * Email: wing.yumin@gmail.com
 */
public class ServerExecute {
    private ServerRead serverRead = new ServerRead();

    public void execute(LeshanServer server, Registration registration, int objectId, int objectInstanceId, int resourceId) throws InterruptedException {
        // Execute Client Resource
        ExecuteRequest executeRequest = new ExecuteRequest(objectId, objectInstanceId, resourceId);
        ExecuteResponse executeResponse = server.send(registration, executeRequest);
        System.out.println("========Execute Resource /"+objectId+"/"+objectInstanceId+"/"+resourceId+ "======");
        if(executeResponse.isSuccess()){
            System.out.println("Execute on : "+registration.getEndpoint());
        } else {
            System.out.println("Failed to Execute.");
        }

    }
}
