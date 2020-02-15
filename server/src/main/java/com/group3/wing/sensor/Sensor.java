package com.group3.wing.sensor;

import com.group3.wing.sensor.objects.SoundSensor;
import org.eclipse.leshan.LwM2mId;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.client.object.Security;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.StaticModel;
import org.eclipse.leshan.core.request.BindingMode;

import java.util.List;

public class Sensor {
    public static void main(String[] args) {
        String endpoint = "Sound Sensor";
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress("localhost", 9090);

        // Create Own Object
        List<ObjectModel> models = ObjectLoader.loadDefault();
        String[] modelPaths = new String[]{"3001.xml"};
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));

        ObjectsInitializer initializer = new ObjectsInitializer(new StaticModel(models));
        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://localhost:4466", 12345));
        initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, 5 * 60, BindingMode.U, false));
        initializer.setInstancesForObject(LwM2mId.DEVICE, new Device("Wing Leshan", "model12345", "12345", "U"));
        initializer.setInstancesForObject(3001, new SoundSensor());
        builder.setObjects(initializer.createAll());

        LeshanClient client = builder.build();
        client.start();
    }
}
