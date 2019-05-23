package com.example.mqttapplication.eventbus;

public class GPSLocateEvent {
    private double lat, lng;

    public GPSLocateEvent(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
