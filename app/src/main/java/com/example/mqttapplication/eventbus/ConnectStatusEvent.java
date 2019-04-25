package com.example.mqttapplication.eventbus;


public class ConnectStatusEvent {
    private boolean isConnected;

    public ConnectStatusEvent(boolean isConnected){
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
