package com.example.mqttapplication.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.example.mqttapplication.adapter.DeviceLogAdapter;
import com.example.mqttapplication.roomdatabase.DeviceEntity;
import com.example.mqttapplication.viewmodel.DeviceLogViewModel;

import java.util.List;

public class DeviceLogActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Toolbar deviceToolbar;
    private String title, hostname;
    private boolean connStatus;
    private int deviceID;
    private DeviceLogViewModel model;

    public DeviceLogActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_log);

        //Get data from Fragment Device List
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        deviceID = intent.getIntExtra("BkgToolbar", 0);

        //Get data from Share Preferences
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        hostname = mqttConnInfo.getString("MQTT_Hostname", "");
        connStatus = mqttConnInfo.getBoolean("connStatus", false);

        //Set data for toolbar
        deviceToolbar = findViewById(R.id.device_log_toolbar);
        setSupportActionBar(deviceToolbar);
        getSupportActionBar().setTitle(title);

        //Set initially subtitle
        if(connStatus){
            deviceToolbar.setSubtitle("Connect to " + hostname);
        }

        else{
            deviceToolbar.setSubtitle("Not found broker MQTT");
        }

        //Call function to set background for Toolbar
        setBackgroundToolbar(deviceID);

        RecyclerView recyclerView = findViewById(R.id.rv_device_log);
        final DeviceLogAdapter adapter = new DeviceLogAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        model = ViewModelProviders.of(this).get(DeviceLogViewModel.class);
        model.getAllData(deviceID).observe(this, new Observer<List<DeviceEntity>>() {
            @Override
            public void onChanged(@Nullable List<DeviceEntity> deviceEntities) {
                adapter.setData(deviceEntities);
            }
        });
    }

    /**
     * set background for toolbar with each device
     * @param colorNum
     */
    private void setBackgroundToolbar(int colorNum){
        switch(colorNum){
            case 1:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_1);
                break;
            case 2:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_2);
                break;
            case 3:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_3);
                break;
            case 4:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_4);
                break;
            case 5:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_5);
                break;
            case 6:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_6);
                break;
            case 7:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_7);
                break;
            case 8:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_8);
                break;
            default:
                deviceToolbar.setBackgroundResource(R.color.colorBackgroundTextDevice_8);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_log, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.dv_log_back){
            super.onBackPressed();
            return true;
        }
        else if(id == R.id.dv_log_delete){
            DeviceLogViewModel model = ViewModelProviders.of(this).get(DeviceLogViewModel.class);
            model.deleteAll(deviceID);
            Toast.makeText(this, "Delete all data in device " + deviceID, Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);

    }
}
