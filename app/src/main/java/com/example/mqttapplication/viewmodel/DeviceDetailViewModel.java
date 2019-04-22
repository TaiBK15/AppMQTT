package com.example.mqttapplication.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.mqttapplication.repository.DeviceRepository;
import com.example.mqttapplication.roomdatabase.EntityDevice_1;

import java.util.List;

public class DeviceDetailViewModel extends AndroidViewModel {

    private DeviceRepository repo;
    private LiveData<List<EntityDevice_1>> latestData;

    public DeviceDetailViewModel(Application application){
        super(application);
        repo = new DeviceRepository(application);
    }

    public void insert(EntityDevice_1 device_1){
        repo.insert(device_1);
    }

    public void deleteAll(){
        repo.deleteAll();
    }

    public LiveData<List<EntityDevice_1>> getLatestData(){
        return repo.getLatestData();
    }

    public LiveData<List<EntityDevice_1>> getAllData(){
        return repo.getAllData();
    }
}
