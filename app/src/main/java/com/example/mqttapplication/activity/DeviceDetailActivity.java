package com.example.mqttapplication.activity;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.example.mqttapplication.eventbus.ACKSwitchEvent;
import com.example.mqttapplication.eventbus.ConnectStatusEvent;
import com.example.mqttapplication.fragment.ChartFragment;
import com.example.mqttapplication.roomdatabase.DeviceEntity;
import com.example.mqttapplication.viewmodel.DeviceDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import mqttsrc.MqttApi;

public class DeviceDetailActivity extends AppCompatActivity implements View.OnLongClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private Toolbar deviceToolbar;
    private String title, hostname, topic;
    private boolean connStatus, swStatus;
    private FrameLayout fr_device_detail;
    private LinearLayout ln_temp, ln_brightness, ln_humidity;
    private ProgressBar proBarHumidity, proBarBrightness, proBarTemperature;
    private TextView tv_proBarHumidity, tv_proBarBrightness, tv_temp;
    private Switch sw_light;
    private int deviceID, temp, bright, humidity;
    private DeviceDetailViewModel deviceDetailViewModel;
    private MqttApi mqttApi;
    private Dialog loadToWait;
    private Thread waitACK;
    private int TIMEOUT = 10;
    private int stickTime = 0;
    private volatile boolean isThreadAlive = false;
    private Context context;

    private final int TEMP = 0;
    private final int BRI = 1;
    private final int HUM = 2;

    public DeviceDetailActivity(){
        context = this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        ln_temp = findViewById(R.id.ln_temp);
        ln_brightness = findViewById(R.id.ln_brightness);
        ln_humidity = findViewById(R.id.ln_humidity);

        //Set long click on linear layout
        ln_temp.setOnLongClickListener(this);
        ln_brightness.setOnLongClickListener(this);
        ln_humidity.setOnLongClickListener(this);

        //Get mqttApi object from MainActivity
        MainActivity mainActivity = new MainActivity();
        mqttApi = mainActivity.getMqttApi();

        //Get data from Fragment Device List
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        deviceID = intent.getIntExtra("BkgToolbar", 0);

        //Get data from Share Preferences
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        hostname = mqttConnInfo.getString("MQTT_Hostname", "");
        connStatus = mqttConnInfo.getBoolean("connStatus", false);

        //Set data for toolbar
        deviceToolbar = findViewById(R.id.device_detail_toolbar);
        fr_device_detail = findViewById(R.id.fr_device_detail);
        setSupportActionBar(deviceToolbar);
        getSupportActionBar().setTitle(title);

        //Set initially subtitle
        if(connStatus){
            deviceToolbar.setSubtitle("Connect to " + hostname);
            fr_device_detail.setVisibility(LinearLayout.VISIBLE);
        }

        else{
            deviceToolbar.setSubtitle("Not found broker MQTT");
            fr_device_detail.setVisibility(LinearLayout.INVISIBLE);
        }

        sw_light = findViewById(R.id.sw_light);
        SharedPreferences switchState = getSharedPreferences("SwitchState", MODE_PRIVATE);
        swStatus = switchState.getBoolean("sw_device_" + deviceID, false);
        sw_light.setChecked(swStatus);
//        sw_light.setEnabled(false);
        sw_light.setClickable(false);

        //Call function to set background for Toolbar
        setBackgroundToolbar(deviceID);

        proBarTemperature = findViewById(R.id.progressBarTemperature);
        tv_temp = findViewById(R.id.tv_temp);
        proBarBrightness = findViewById(R.id.progressBarBrightness);
        tv_proBarBrightness = findViewById(R.id.tv_progressBarBrightness);
        proBarHumidity = findViewById(R.id.progressBarHumidity);
        tv_proBarHumidity = findViewById(R.id.tv_progressBarHumidity);

        deviceDetailViewModel = ViewModelProviders.of(this).get(DeviceDetailViewModel.class);
        deviceDetailViewModel.getLatestData(deviceID).observe(this, new Observer<DeviceEntity>() {
            @Override
            public void onChanged(@Nullable DeviceEntity device_Entity) {
                if(device_Entity != null){
                    Log.d(TAG, device_Entity.getTime());
                    //Set data for sensor Temperature
                    proBarTemperature.setProgress(device_Entity.getTemp());
                    tv_temp.setText(device_Entity.getTemp() + "\u2103");

                    //Set data for sensor Brightness
                    proBarBrightness.setProgress(device_Entity.getBright());
                    tv_proBarBrightness.setText("" + device_Entity.getBright() + "%");

                    //Set data for sensor Humidity
                    proBarHumidity.setProgress(device_Entity.getHumidity());
                    tv_proBarHumidity.setText("" + device_Entity.getHumidity() + "%");
                }
                else{
                    //Set data for sensor Temperature
                    proBarTemperature.setProgress(0);
                    tv_temp.setText(0 + "\u2103");

                    //Set data for sensor Brightness
                    proBarBrightness.setProgress(0);
                    tv_proBarBrightness.setText("" + 0 + "%");

                    //Set data for sensor Humidity
                    proBarHumidity.setProgress(0);
                    tv_proBarHumidity.setText("" + 0 + "%");
                }
            }

        });

        //Set on click switch
        sw_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swStatus = sw_light.isChecked();
                mqttApi = MainActivity.getMqttApi();
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("topic", "device" + "\\/req");
                    jsonObject.put("device_id", deviceID);
                    jsonObject.put("switch_state", swStatus);
                    mqttApi.publishToTopic(jsonObject.toString().replace("\\",""), 0, "device/req");
//                    mqttApi.publishToTopic(jsonObject.toString(), 0, "device/req");
                    Log.d(TAG, jsonObject.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
                showWaitingSwitchACK();
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Subscribe connect event from main activity
     * @param connectStatusEvent
     */
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void onEvent(ConnectStatusEvent connectStatusEvent){

        if(connectStatusEvent.isConnected()){
            deviceToolbar.setSubtitle("Connect to " + hostname);
            fr_device_detail.setVisibility(LinearLayout.VISIBLE);
        }
        else{
            deviceToolbar.setSubtitle("Not found broker MQTT");
            fr_device_detail.setVisibility(LinearLayout.INVISIBLE);
        }
    }

    /**
     * Subscribe switch state ack event from main activity
     * @param ackSwitchEvent
     */
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void onEvent(ACKSwitchEvent ackSwitchEvent){
        if(ackSwitchEvent.getSwID() == deviceID){
            sw_light.setChecked(ackSwitchEvent.isSwState());
            Toast.makeText(this, "Control successfully", Toast.LENGTH_LONG).show();
            if(isThreadAlive){
                isThreadAlive = false;
                if(loadToWait != null)
                    loadToWait.cancel();
            }
        }

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.ln_temp:
                goToChartFragment(TEMP);
                break;
            case R.id.ln_brightness:
                goToChartFragment(BRI);
                break;
            case R.id.ln_humidity:
                goToChartFragment(HUM);
                break;
        }
        return false;
    }

    /**
     * Go to Chart Fragment
     */
    private void goToChartFragment(int type){
        Bundle bundle =  new Bundle();
        bundle.putInt("deviceID", deviceID);
        bundle.putInt("typeChart", type);

        ChartFragment chartFragment = new ChartFragment();
        chartFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_device_detail, chartFragment)
                .addToBackStack(null);
        transaction.commit();
    }

    private void showWaitingSwitchACK(){
        loadToWait = new Dialog(this);
        loadToWait.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadToWait.setContentView(R.layout.dialog_waiting_ack);
        loadToWait.setCancelable(false);
        loadToWait.setCanceledOnTouchOutside(false);
        loadToWait.show();
        isThreadAlive = true;
        waitACK = new Thread(){
            @Override
            public void run() {
                super.run();
                while(isThreadAlive){
                    Log.d(TAG, "Thread exist = " + isThreadAlive);
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                    stickTime++;
                    if(stickTime == TIMEOUT){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sw_light.setChecked(!sw_light.isChecked());
                                loadToWait.cancel();
                                Toast.makeText(context, "Control Failed!", Toast.LENGTH_LONG).show();
                            }
                        });
                        isThreadAlive = false;
                    }
                    Log.d(TAG, "time = " + stickTime);
                }
                cleanThread();
            }
        };
        waitACK.start();
    }


    private void cleanThread(){
        if(waitACK != null) {
            if (waitACK.isAlive()) {
                Log.d(TAG, "interrupt thread");
                Thread.currentThread().interrupt();
                waitACK = null;
            }
        }
        stickTime = 0;
    }
}
