package com.project.app;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.util.NamedThreadFactory;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;


/**
 * Created by tyang on 11/7/19.
 */
public class SoundDetector extends BaseInstanceEnabler {

    private static final String UNIT_DB = "db";
    private static final int SETTING = 0;
    private static final int ACTION = 1;
    private static final int TIME = 51;
    private static final int MODE = 1;
    private static final int SENSOR_VALUE = 2;
    private static final int UNITS = 54;
    private static final int ENDPOINT = 3;
    // since defualt is that app starts at setting mode;
    // 0 for setting mode
    // 1 for action mode;
    private int _mode = SETTING;
    private String _timestamp;
    private String _endpoint;
    private String _data = "";
    private static final List<Integer> supportedResources = Arrays.asList(SETTING,ACTION,TIME,MODE,SENSOR_VALUE,UNITS,ENDPOINT);
    public MCP3008Gpio m = new MCP3008Gpio();

    public SoundDetector() {
       /* this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Sound Sensor"));
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                sendSounds();
            }
        }, 2, 2, TimeUnit.SECONDS);*/

        ExecutorService executorService = Executors.newFixedThreadPool(1); // number of threads
        // declare variables as final which will be used in method run below
        
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while(true){
                    sendSounds();
                }
            }
        });       
    }


    // adjust the sound according to max and min mesruesed
    private void sendSounds(){
        String str = getdbPoints();
        JSONObject json = new JSONObject(str);
        _mode = json.getInt("mode");

        if(_mode == 1){
            _endpoint = json.getString("target");
        }

        _data = json.getString("data");

        // if (str != null) {
        //     fireResourcesChange(SENSOR_VALUE, json.getString("data"));
        // } else {
        //     fireResourcesChange(SENSOR_VALUE);
        // }
        System.out.println("Notify");
        fireResourcesChange(SENSOR_VALUE);
    }

    // this should actullay get the real data from cai
    // now just fake ones
    // return a string colletion of db points
    public String getdbPoints() {
        m.setup();
        
        try{
            
            JSONObject  res = m.reutrnData();
            return res.toString();
            
        }catch (Exception e){ 
            e.printStackTrace();
            return e.toString();
        }
    }

    public String getData(){
        return _data;
    }
    public int getMode() {
        return _mode;
    }

    public void setMode(int mode) {
        this._mode = mode;
    }

    public String getTimestamp() {
         
        _timestamp = Long.toString(System.currentTimeMillis());
        return _timestamp;
    }

    public String getEndPoint() {
        return _endpoint;
    }

    public void setEndPoint(String endpoint) {
        this._endpoint = endpoint;
    }

    public void setTimestamp(String _timestamp) {
        _timestamp = Long.toString(System.currentTimeMillis());
        this._timestamp = _timestamp;
    }

   /* @Override
    public String toString() {
        return "SoundDetecor [_timestamp=" + _timestamp + ", _dbPoint=" + Arrays.toString(_dbPoints) + ", _Mode=" + _mode + "]";
    }
*/

    @Override
    public synchronized ReadResponse read(ServerIdentity identity, int resourceId) {
        switch (resourceId) {
            case TIME:
                return ReadResponse.success(resourceId, getTimestamp());
            case MODE:
                return ReadResponse.success(resourceId, getMode());
            case SENSOR_VALUE:
                return ReadResponse.success(resourceId, getData());
            case UNITS:
                return ReadResponse.success(resourceId, UNIT_DB);
            case ENDPOINT:
                return ReadResponse.success(resourceId, getEndPoint());
            default:
                return super.read(identity, resourceId);
        }
    }
    @Override
    public List<Integer> getAvailableResourceIds(ObjectModel model) {
        return supportedResources;
    }

}
