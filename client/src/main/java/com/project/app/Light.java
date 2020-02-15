package com.project.app;

import org.eclipse.leshan.client.request.ServerIdentity;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import com.project.model.SqlDB;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Created by tyang on 11/10/19.
 */
public class Light extends BaseInstanceEnabler {
    private long current;
    private int lightStatus = 0;
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED", PinState.LOW);
    // private boolean = false;
    // 0 for dim 
    // 1 for light
    private static final int STATE = 1;
    private static final List<Integer> supportedResources = Arrays.asList(STATE);

    public Light(){
            led.setShutdownOptions(true, PinState.LOW);
            current = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, String params) {
        switch (resourceid){
            case 1:
                //this.lightStatus = lightStatus == 0? 1 : 0;
                if(Calendar.getInstance().getTimeInMillis()-current>1000){
                    System.out.println("Ececute");
                    trigger();
                    
                    current = Calendar.getInstance().getTimeInMillis();
                }else{
                    System.out.println("Multiple Ececute");
                }
                return ExecuteResponse.success();
            default:
                return super.execute(identity, resourceid, params);
        }
    }

    public void trigger(){
        led.toggle();
        if(lightStatus == 0){
            lightStatus = 1;
            SqlDB.getInstance().ececuteRecord("Turn on Light 1");
        }else{
            lightStatus = 0;
            SqlDB.getInstance().ececuteRecord("Turn off Light 1");
        }
    }
}
