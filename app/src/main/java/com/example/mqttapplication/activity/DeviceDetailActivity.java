package com.example.mqttapplication.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mqttapplication.R;
import com.example.mqttapplication.viewmodel.DeviceDetailActivityViewModel;

import mqttsrc.MqttApi;

public class DeviceDetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Toolbar deviceToolbar;
    private String title, hostname;
    private int background;
    private boolean connStatus;
    private ProgressBar proBarHumidity, proBarBrightness, proBarTemperature;
    private TextView tv_proBarHumidity, tv_proBarBrightness, tv_temp;
    private Switch sw_light;
    private MqttApi mqttApi;
    private DeviceDetailActivityViewModel model;

//    private static DeviceDetailActivity instance;

    //No outer class can create object via this construtor method
//    private DeviceDetailActivity(){
//
//    }
//
//    public static DeviceDetailActivity getInstance(){
//        if(instance == null){
//            instance = new DeviceDetailActivity();
//        }
//        return instance;
//    }

    public DeviceDetailActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        //Get mqttApi object from MainActivity
        MainActivity mainActivity = new MainActivity();
        mqttApi = mainActivity.getMqttApi();

        //Get data from Fragment Device List
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        background = intent.getIntExtra("BkgToolbar", 0);

        //Get data from Share Preferences
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        hostname = mqttConnInfo.getString("MQTT_Hostname", "");
        connStatus = mqttConnInfo.getBoolean("connStatus", false);

        //Set data for toolbar
        deviceToolbar = findViewById(R.id.device_detail_toolbar);
        setSupportActionBar(deviceToolbar);
        getSupportActionBar().setTitle(title);
        //Set initially subtitle
        if(connStatus)
            deviceToolbar.setSubtitle("Connect to " + hostname);
        else
            deviceToolbar.setSubtitle("Not found broker MQTT");

        //Create viewmodel object
//        model = ViewModelProviders.of(this).get(DeviceDetailActivityViewModel.class);
//        model.getConnStatus().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
//                if(aBoolean)
//                    deviceToolbar.setSubtitle("Connect to " + hostname);
//                else
//                    deviceToolbar.setSubtitle("Not found broker MQTT");
//            }
//        });

        //Call function to set background for Toolbar
        setBackgroundToolbar(background);

        //Set data for sensor Temperature
        proBarTemperature = findViewById(R.id.progressBarTemperature);
        tv_temp = findViewById(R.id.tv_temp);
        proBarTemperature.setProgress(100);
        tv_temp.setText(100 + "\u2103");

        //Set data for sensor Brightness
        proBarBrightness = findViewById(R.id.progressBarBrightness);
        tv_proBarBrightness = findViewById(R.id.tv_progressBarBrightness);
        proBarBrightness.setProgress(70);
        tv_proBarBrightness.setText("" + 70 + "%");

        //Set data for sensor Humidity
        proBarHumidity = findViewById(R.id.progressBarHumidity);
        tv_proBarHumidity = findViewById(R.id.tv_progressBarHumidity);
        proBarHumidity.setProgress(50);
        tv_proBarHumidity.setText("" + 50 + "%");

        //Set on click switch
        sw_light = findViewById(R.id.sw_light);
        sw_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = sw_light.isChecked();
//                mqttApi.publishToTopic(title, 0, "AppMQTT");
//                if(onClick == null)
//                    Log.d(TAG, "interface null");
//                else
//                    onClick.onClickSwitch(title, state);


            }
        });

//        MainActivity event = new MainActivity();
//        event.setConnectListener(new ConnectStatusListenerInterface() {
//            @Override
//            public void setConnectStatus(boolean isConnected) {
//                if(isConnected)
//                    deviceToolbar.setSubtitle("Connect to " + hostname);
//                else
//                    deviceToolbar.setSubtitle("Not found broker MQTT");
//            }
//        });

    }

    /**
     * set background for toolbar with each device
     * @param colorNum
     */
    private void setBackgroundToolbar(int colorNum){
        switch(background){
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
        getMenuInflater().inflate(R.menu.menu_device_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.device_item_menu){
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
//
//    public void setConnectToDevice(Boolean isConnected){
//        model = ViewModelProviders.of(this).get(DeviceDetailActivityViewModel.class);
//        model.setConnStatus(isConnected);
//    }

//    @Override
//    public void setConnectStatus(boolean isConnected) {
//        if(isConnected)
//            deviceToolbar.setSubtitle("Connect to " + hostname);
//        else
//            deviceToolbar.setSubtitle("Not found broker MQTT");
//    }

//    public final void setOnClick(OnClickSwitchListener onClick){
//        this.onClick = onClick;
//    }
//
//    interface OnClickSwitchListener{
//        void onClickSwitch(String deviceName, boolean on_off);
//    }
}
