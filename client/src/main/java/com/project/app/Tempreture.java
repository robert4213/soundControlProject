package com.project.app;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.resource.ResourceChangedListener;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.util.NamedThreadFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wing Yu on 10/29/2019 5:20 PM).
 * Blog: http://www.wingyu.org/
 * GitHub: https://github.com/wing324
 * Email: wing.yumin@gmail.com
 */
public class Tempreture extends BaseInstanceEnabler {

    private static final String UNIT_CELSIUS = "cel";
    private static final int SENSOR_VALUE = 5700;
    private static final int UNITS = 5701;
    private static final int MAX_MEASURED_VALUE = 5602;
    private static final int MIN_MEASURED_VALUE = 5601;
    private static final int RESET_MIN_MAX_MEASURED_VALUES = 5605;
    private static final int WRITE_TEST = 5800;
    private static final List<Integer> supportedResources = Arrays.asList(SENSOR_VALUE, UNITS, MAX_MEASURED_VALUE,
            MIN_MEASURED_VALUE, RESET_MIN_MAX_MEASURED_VALUES);
    private String units;
    private String minMeasuredValue;
    private String maxMeasuredValue="maxMeasuredValue";
    private int sensorValue;
    private boolean flag;
    private final ScheduledExecutorService scheduler;
    
    public Tempreture() {
    	sensorValue=100;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Temperature Sensor"));
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                sensorValue+=10;
                fireResourcesChange(SENSOR_VALUE, sensorValue);
            }
        }, 2, 5, TimeUnit.SECONDS);
    }


    @Override
    public ReadResponse read(ServerIdentity identity, int resourceId) {
        System.out.println("Read Sensor Resource: " + resourceId);
        switch (resourceId)
        {
            case MIN_MEASURED_VALUE:
                return ReadResponse.success(resourceId, "ClientValue");
            case MAX_MEASURED_VALUE:
                return ReadResponse.success(resourceId, maxMeasuredValue);
            case SENSOR_VALUE:
                return ReadResponse.success(resourceId, Integer.toString(sensorValue));
            case UNITS:
                return ReadResponse.success(resourceId, UNIT_CELSIUS);
            default:
                return super.read(identity, resourceId);
        }
    }

    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceId, String params) {
        System.out.println("Execute Tempreture resource " + resourceId);
        switch (resourceId){
            case RESET_MIN_MAX_MEASURED_VALUES:
                resetMinMaxMeasuredValues();
                return ExecuteResponse.success();
            default:
                return super.execute(identity, resourceId, params);
        }
    }

    @Override
    public WriteResponse write(ServerIdentity identity, int resourceid, LwM2mResource value) {
        System.out.println(value.getValue());
        return WriteResponse.success();
    }

    private void resetMinMaxMeasuredValues() {
        minMeasuredValue = "0";
        maxMeasuredValue = "0";
    }

    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }

    public void updateValue() {
        sensorValue +=10;
        fireResourcesChange(SENSOR_VALUE, sensorValue);
    	flag = true;
//    	System.out.println("Sensor update");
    }


}
