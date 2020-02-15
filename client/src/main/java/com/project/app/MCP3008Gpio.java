package com.project.app;

import com.project.app.ButtonCommand;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.util.Console;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.project.model.SqlDB;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Shiyan on 11/1/19.
 */

public class MCP3008Gpio {
    public  SpiDevice spi = null;
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
    final ButtonCommand btn = new ButtonCommand();
    final PinState myButtonState = myButton.getState();
    private final static int THREADHOLD = 700;
    private final static int GAP = 4;
    final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
    
    

    // ADC channel count
    public  short ADC_CHANNEL_COUNT = 8;  // MCP3004=4, MCP3008=8

    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    protected   Console console = new Console();


    public JSONObject reutrnData()throws InterruptedException, IOException{
        JSONObject result = new JSONObject();
        StringBuilder str = new StringBuilder();
        int count = -1;
        int data = 0;
        String target = "";

        boolean state = false;

        spi = SpiFactory.getInstance(SpiChannel.CS0,
            SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
            SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0    

    // continue reading
        while(true) {
            
            // System.out.println("State: "+ state.toString());
                        
            data = read();
            // Thread.sleep(100);
            if(count == -1){
                if(data >= THREADHOLD){
                    str.append(Integer.toString(data));
                    count++;
                }
            }else if(count<GAP){
                str.append(","+Integer.toString(data));
                if(data >= THREADHOLD){                    
                    count = 0;
                }else{
                    count++;
                }                
            }else{
                break;
            }

            if(!state){
                target = btn.buttonRead();
                if(!target.equals("")){
                    state = true;
                    str = new StringBuilder();
                    count = -1;
                    led.high();
                    SqlDB.getInstance().ececuteRecord("Light Indicator");
                    Thread.sleep(1000);
                    // led.toggle();
                    
                }
            }
                        
            if(count != -1){
                System.out.println(String.format("Result: %04d", data));
                System.out.println("  Count Down" + Integer.toString(GAP - count));                
            }else{
                System.out.println(String.format("Result: %04d",data));
            }
            // console.emptyLine();
        }        
        led.low();
        if(state){
            result.put("mode",1);
            result.put("target",target);
        }else{
            result.put("mode",0);
        }
        result.put("data",str.toString());
        System.out.println("mode: "+ result.getInt("mode"));
        System.out.println("data" + str.toString());
        return result;
    }


    public int read() throws IOException, InterruptedException {
        long start =  Calendar.getInstance().getTimeInMillis();
        int max = 0;
        int min = 1024;
        while ((Calendar.getInstance().getTimeInMillis() - start)<500){
            int conversion_value = getConversionValue((short)0);
            if(conversion_value > max) max = conversion_value;
            if(conversion_value < min) min = conversion_value;
        }
        return max-min;
    }

    public  int getConversionValue(short channel) throws IOException {

        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[] {
                (byte) 0b00000001,                              // first byte, start bit
                (byte)(0b10000000 |( ((channel & 7) << 4))),    // second byte transmitted -> (SGL/DIF = 1, D2=D1=D0=0)
                (byte) 0b00000000                               // third byte transmitted....don't care
        };

        // send conversion request to ADC chip via SPI channel
        byte[] result = spi.write(data);

        // calculate and return conversion value from result bytes
        int value = (result[1]<< 8) & 0b1100000000; //merge data[1] & data[2] to get 10-bit result
        value |=  (result[2] & 0xff);
        return value;
    }

    public void setup(){
        myButton.setShutdownOptions(true);
        led.setShutdownOptions(true, PinState.LOW);
    }
}