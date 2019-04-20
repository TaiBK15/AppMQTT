package com.example.mqttapplication.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DeviceDetailActivityViewModel extends ViewModel {
    final String TAG = this.getClass().getSimpleName();

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
