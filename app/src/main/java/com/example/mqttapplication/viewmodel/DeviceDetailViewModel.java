package com.example.mqttapplication.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.mqttapplication.repository.DeviceRepository;
import com.example.mqttapplication.roomdatabase.DeviceEntity;

import java.util.List;

public class DeviceDetailViewModel extends AndroidViewModel {

    private DeviceRepository repo;

    public DeviceDetailViewModel(Application application){
        super(application);
        repo = new DeviceRepository(application);
    }

    public void insert(DeviceEntity device_1){
        repo.insert(device_1);
    }

    public void deleteAll(int deviceID){
        repo.deleteAll(deviceID);
    }

    public LiveData<DeviceEntity> getLatestData(int deviceID){
        return repo.getLatestData(deviceID);
    }

    public LiveData<List<DeviceEntity>> getAllData(int deviceID){
        return repo.getAllData(deviceID);
    }
}
