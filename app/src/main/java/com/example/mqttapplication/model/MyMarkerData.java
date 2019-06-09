package com.example.mqttapplication.model;

import com.google.android.gms.maps.model.LatLng;

public class MyMarkerData {
    private LatLng latLng;
    private String title;

    public MyMarkerData(LatLng latLng, String title){
        this.latLng = latLng;
        this.title = title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTitle() {
        return title;
    }
}
