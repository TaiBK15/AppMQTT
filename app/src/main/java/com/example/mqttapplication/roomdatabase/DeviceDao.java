package com.example.mqttapplication.roomdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DeviceDao {
    @Insert
    void insert(DeviceEntity device_1);

    @Update
    void update(DeviceEntity device_1);

    @Delete
    void delete(DeviceEntity device_1);

    @Query("DELETE FROM MQTT_DEVICE WHERE deviceID = :deviceID")
    void deleteAll(int deviceID);

    @Query("DELETE FROM MQTT_DEVICE")
    void deleteDatabase();

    @Query("SELECT * FROM MQTT_DEVICE WHERE deviceID = :deviceID ORDER BY id DESC LIMIT 1")
    LiveData<DeviceEntity> getLatestData(int deviceID);

    @Query("SELECT * FROM MQTT_DEVICE WHERE deviceID = :deviceID ORDER BY id DESC")
    LiveData<List<DeviceEntity>> getAllData(int deviceID);
}
