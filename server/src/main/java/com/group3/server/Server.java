package com.group3.server;

import com.group3.server.ir.ServerObserve;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.model.LwM2mModelProvider;
import org.eclipse.leshan.server.model.VersionedModelProvider;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;

import java.util.Collection;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        String[] modelPaths = new String[]{"3001.xml", "3002.xml"};
        // Define model provider
        List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models/", modelPaths));
        LwM2mModelProvider modelProvider = new VersionedModelProvider(models);
        LeshanServerBuilder builder = new LeshanServerBuilder();
        builder.setLocalAddress("172.20.10.11", 4466);
        builder.setObjectModelProvider(modelProvider);
        final LeshanServer server = builder.build();
        server.start();

        final ServerObserve serverObserve = new ServerObserve();

        server.getRegistrationService().addListener(new RegistrationListener() {
            public void registered(Registration registration, Registration registration1, Collection<Observation> collection) {
                System.out.println("New Device Register: " + registration.getEndpoint() + "  Device ID is: "+ registration.getId());

                if(registration.getEndpoint().equals("Client1")){
                    try {
                        // Observe An Object
//                        ReadResponse response = server.send(registration, new ReadRequest(3001,0,1));
//                        System.out.println(response.toString());
                        serverObserve.observe(server, server.getRegistrationService().getByEndpoint("Client1"), 3001, 0, 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void updated(RegistrationUpdate registrationUpdate, Registration registration, Registration registration1) {

            }

            public void unregistered(Registration registration, Collection<Observation> collection, boolean b, Registration registration1) {

            }
        });
    }
}
