package com.example.mqttapplication.eventbus;

public class ACKSwitchEvent {
    private boolean swState;
    private int swID;

    public ACKSwitchEvent(int swID, boolean swState){
        this.swID = swID;
        this.swState = swState;
    }

    public boolean isSwState() {
        return swState;
    }

    public int getSwID() {
        return swID;
    }
}
