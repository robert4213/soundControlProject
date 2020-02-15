package com.group3.wing.sensor.objects;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.util.NamedThreadFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SoundSensor extends BaseInstanceEnabler {
    private static final List<Integer> supportedResources = Arrays.asList(1, 2, 3);
    private Random random = new Random();
    private final ScheduledExecutorService scheduler;
    private int mode = 0;
    private String endpoint = "Light B";
    private StringBuilder sensorValue;


    public SoundSensor() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Sounds Sensor"));
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                adjustSounds();
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    private void adjustSounds(){
        getSensorValue();
        fireResourcesChange(2);
    }

    public String getSensorValue() {
        sensorValue = new StringBuilder();
        for(int i = 0; i < 20; i++){
            if(i>0){
                sensorValue.append(",");
            }
            int value = random.nextInt(1300);
            sensorValue.append(value);
        }
        return sensorValue.toString();
    }

    public int getMode() {
        return mode;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        switch (resourceid){
            case 1:
                return ReadResponse.success(resourceid, getMode());
            case 2:
                return ReadResponse.success(resourceid, getSensorValue());
            case 3:
                return ReadResponse.success(resourceid, getEndpoint());
            default:
                return super.read(identity, resourceid);
        }
    }


}
