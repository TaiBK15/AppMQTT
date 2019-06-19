package com.example.mqttapplication.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class MyMarkerData {
    private LatLng latLng;
    private String title;
    private Bitmap bitmap;

    public MyMarkerData(LatLng latLng, String title, Bitmap bitmap){
        this.latLng = latLng;
        this.title = title;
        this.bitmap = bitmap;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
