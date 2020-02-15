package com.group3.server.ir;

import com.group3.server.ClapCount;
import com.group3.server.dm.ServerExecute;
import com.group3.server.dm.ServerRead;
import com.group3.tools.MongoDBCRUD;
import com.mongodb.client.MongoCursor;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.bson.Document;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.eclipse.leshan.server.registration.Registration;

public class ObserveListener {
    private ServerRead serverRead = new ServerRead();
    private ServerExecute serverExecute = new ServerExecute();
    private int claps;
    private int mode;
    private String endpoint;
    MongoDBCRUD mongoDB = new MongoDBCRUD("server", "claps");
    public ObservationListener observationListener(final LeshanServer server){
        return new ObservationListener() {
            public void newObservation(Observation observation, Registration registration) {
                System.out.println("Observing resource " + observation.getPath() + " from client "
                        + registration.getEndpoint());
            }

            public void cancelled(Observation observation) {

            }

            public void onResponse(Observation observation, Registration registration, ObserveResponse observeResponse) {
                System.out.println("Received notification from "+observation.getPath()+ " containing value :"+
                        observeResponse.getContent().toString());
                String sensorValue = ((LwM2mResource)observeResponse.getContent()).getValue().toString();
                claps = new ClapCount().count(sensorValue);
                try {
                    mode = serverRead.readValue(server, server.getRegistrationService().getByEndpoint("Client1"), 3001, 0, 1);
                    System.out.println("Mode is: "+mode);
                    if(mode == 1){
                        // setting mode
                        endpoint = serverRead.readStringValue(server, server.getRegistrationService().getByEndpoint("Client1"), 3001, 0, 3);
                        // update endpoint claps in mongoDB
                        mongoDB.updateClaps(endpoint, claps);
                        System.out.println("Setting endpoint: "+endpoint+" to "+claps+" claps");
                    } else {
                        // non-setting mode
                        MongoCursor<Document> cursor = mongoDB.getEndpointByClaps(claps);
                        if(cursor.hasNext()){
                            endpoint = (String) cursor.next().get("endpoint");
                            System.out.println("Execute on: "+endpoint);
                            serverExecute.execute(server, server.getRegistrationService().getByEndpoint(endpoint), 3002, 0, 1);
                        } else {
                            System.out.println("Can not find a client with the claps.");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }

            public void onError(Observation observation, Registration registration, Exception e) {
                System.out.println("Observation is error: "+e.getMessage());
            }
        };
    }
}
