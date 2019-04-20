package com.example.mqttapplication.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;

import com.example.mqttapplication.activity.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class ConnectStatusViewModel extends ViewModel {
    private final String TAG = "ConnectStatusViewModel";
    private MutableLiveData<Boolean> connStatus;

    public MutableLiveData<Boolean> getConnStatus() {
        if (connStatus == null){
            connStatus = new MutableLiveData<>();
        }
        return connStatus;
    }

    public void setConnStatus(Boolean isConnected) {
        connStatus.setValue(isConnected);
    }
}
