package com.group3.server;

public class ClapCount {
    public int count(String sensorValue){
        int result = 0;
        int previousValue = Integer.MIN_VALUE;
        for(String s : sensorValue.split(",")){
            int value = Integer.parseInt(s);
            if(previousValue < 700 && value >=700){
                result++;
            }
            previousValue = value;
        }

        return result;
    }
}
