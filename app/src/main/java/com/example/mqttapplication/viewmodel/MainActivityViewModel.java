package com.example.mqttapplication.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.mqttapplication.activity.MainActivity;
import com.example.mqttapplication.repository.DeviceRepository;
import com.example.mqttapplication.roomdatabase.DeviceEntity;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainActivityViewModel extends AndroidViewModel {
    private final String TAG = "MainActivityViewModel";
    private MutableLiveData<Boolean> connStatus;
    private DeviceRepository repo;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new DeviceRepository(application);
    }

    public MutableLiveData<Boolean> getConnStatus() {
        if (connStatus == null){
            connStatus = new MutableLiveData<>();
        }
        return connStatus;
    }

    public void setConnStatus(Boolean isConnected) {
        connStatus.setValue(isConnected);
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
