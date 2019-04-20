package com.example.mqttapplication.eventbus;

import com.example.mqttapplication.activity.ConnectStatusListenerInterface;

public class ConnectStatusEvent {
    private boolean isConnected;

    public ConnectStatusEvent(boolean isConnected){
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
