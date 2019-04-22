package com.example.mqttapplication.roomdatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "device_1")
public class EntityDevice_1 {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "temp")
    private int temp;

    @ColumnInfo(name = "bright")
    private int bright;

    @ColumnInfo(name = "humidity")
    private int humidity;

    public EntityDevice_1(int temp, int bright, int humidity) {
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

    public void setTime(String time) {
        this.time = time;
    }
}
