package com.group3.server.ir;

import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.registration.Registration;

public class ServerObserve {
    ObserveListener observeListener = new ObserveListener();
    public void observe(LeshanServer server, Registration registration, int objectId, int objectInstanceId, int resourceid) throws InterruptedException {
        // Observe Object
        ObserveRequest observeRequest = new ObserveRequest(objectId, objectInstanceId, resourceid);
        ObserveResponse observeResponse = server.send(registration, observeRequest);
        System.out.println("=====Observe an Object /"+objectId+"/"+objectInstanceId+"/"+resourceid+"=======");
        if(observeResponse.isSuccess()){
            System.out.println(observeResponse.toString());
            server.getObservationService().addListener(observeListener.observationListener(server));
        } else {
            System.out.println("Failed to Observe.");
        }
    }
}
