package com.group3.wing.lightB.objects;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;

import java.util.Arrays;
import java.util.List;

public class LightStatus extends BaseInstanceEnabler {
    private static final List<Integer> supportedResources = Arrays.asList(1);
    private int lightStatus = 0;

        @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, String params) {
        switch (resourceid){
            case 1:
                assert params != null;
                this.lightStatus = lightStatus == 0? 1 : 0;
                System.out.println("Exexute Light B Status is: " + this.lightStatus);
                return ExecuteResponse.success();
            default:
                return super.execute(identity, resourceid, params);
        }
    }


}
