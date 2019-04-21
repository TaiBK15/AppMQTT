package com.example.mqttapplication.eventbus;

public class SwitchEvent {
    private boolean isOn;

    public SwitchEvent(boolean isOn){
        this.isOn = isOn;
    }

    public boolean getIsOn(){
        return isOn;
    }
}
