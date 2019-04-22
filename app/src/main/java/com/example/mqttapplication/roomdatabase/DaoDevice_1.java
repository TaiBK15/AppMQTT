package com.example.mqttapplication.roomdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoDevice_1{
    @Insert
    void insert(EntityDevice_1 device_1);

    @Update
    void update(EntityDevice_1 device_1);

    @Delete
    void delete(EntityDevice_1 device_1);

    @Query("DELETE FROM device_1")
    void deleteAll();

    @Query("SELECT * FROM device_1 ORDER BY id DESC LIMIT 1")
    LiveData<List<EntityDevice_1>> getLatestData();

    @Query("SELECT * FROM device_1 ORDER BY id DESC")
    LiveData<List<EntityDevice_1>> getAllData();
}
