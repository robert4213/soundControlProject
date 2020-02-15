package com.group3.server.ir;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.CancelObservationRequest;
import org.eclipse.leshan.core.response.CancelObservationResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.registration.Registration;

import java.util.Arrays;

public class CancelObserve {
    public void cancel(LeshanServer server, Registration registration,Observation observation) throws InterruptedException {
        // Cancel Observation
        CancelObservationRequest cancelObservationRequest = new CancelObservationRequest(observation);
        CancelObservationResponse cancelObservationResponse = server.send(registration, cancelObservationRequest);
        System.out.println("=====Cancel Observation /"+ Arrays.toString(observation.getId()) +"=======");
        if(cancelObservationResponse.isSuccess()){
            System.out.println(cancelObservationResponse.toString());
        } else {
            System.out.println("Failed to Cancel Observation");
        }

    }
}
