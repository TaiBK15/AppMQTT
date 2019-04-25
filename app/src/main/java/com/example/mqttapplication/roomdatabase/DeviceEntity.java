package com.example.mqttapplication.roomdatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "MQTT_DEVICE")
public class DeviceEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "deviceID")
    private int deviceID;

    @ColumnInfo(name = "temp")
    private int temp;

    @ColumnInfo(name = "bright")
    private int bright;

    @ColumnInfo(name = "humidity")
    private int humidity;

    public DeviceEntity(String time, int deviceID, int temp, int bright, int humidity) {
        this.time = time;
        this.deviceID = deviceID;
        this.temp = temp;
        this.bright = bright;
        this.humidity = humidity;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
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

    public void setId(int id) {
        this.id = id;
    }

}
