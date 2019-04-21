package com.example.mqttapplication.eventbus;

public class DataReceiveEvent {
    private int deviceID, temp, bright, humidity;

    public DataReceiveEvent(int deviceID, int temp, int bright, int humidity){
        this.deviceID = deviceID;
        this.temp = temp;
        this.bright = bright;
        this.humidity = humidity;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public int getTemp() {
        return temp;
    }

    public int getBright() {
        return bright;
    }

    public int getHumidity() {
        return humidity;
    }
}
