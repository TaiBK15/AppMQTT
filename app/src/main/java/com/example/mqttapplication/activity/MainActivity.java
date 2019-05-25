package com.example.mqttapplication.activity;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.example.mqttapplication.adapter.ViewPagerAdapter;
import com.example.mqttapplication.eventbus.ConnectStatusEvent;
import com.example.mqttapplication.eventbus.GPSLocateEvent;
import com.example.mqttapplication.fragment.ConnectStatusFragment;
import com.example.mqttapplication.fragment.DeviceListFragment;
import com.example.mqttapplication.fragment.MapFragment;
import com.example.mqttapplication.roomdatabase.DeviceEntity;
import com.example.mqttapplication.viewmodel.MainActivityViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import mqttsrc.MqttApi;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivityDebug";

    //Main activity
    private TabLayout tablayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    //Dialog
    private Dialog dialog_setting, dialog_animation;
    //Dialog connect setting
    private EditText edt_hostname, edt_port, edt_username, edt_password;
    private Button btn_save, btn_cancel;
    private CheckBox checkbox;
    private String hostname, port, username, password;
    private boolean chkShowPassword;
    //Dialog connect animation
    private ImageView imgConnAnimation;
    AnimationDrawable wifiAnimation;
    //MQTT Object
    private MqttAndroidClient mqttAndroidClient;
    private static MqttApi mqttApi;
    //Connect status viewmodel
    private MainActivityViewModel model;
    //Flag check activity is running ?
    private boolean isRunning;
    //JSON components
    private int deviceID, temp, bright, humidity;
    //Calendar
    private Calendar calendar;
    private String currentTime;
    //Position
    private double lat, lng;
    //Fire Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mGetReference;
    private ValueEventListener eventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect to mqtt server in the first time
        restorePreference();
        if (!(hostname.equals("") || port.equals("") || username.equals("") || password.equals("")))
            startMQTT();

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo_bk_small);

        tablayout = (TabLayout) findViewById(R.id.tablayout_main_activity);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add fragment
        adapter.addFragment(new ConnectStatusFragment(), "Connect");
        adapter.addFragment(new DeviceListFragment(), "Device");
        adapter.addFragment(new MapFragment(), "Position");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        //Add icon for tablayout
        tablayout.getTabAt(0).setIcon(R.drawable.ic_wifi);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_list);
        tablayout.getTabAt(2).setIcon(R.drawable.ic_gps);

    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                if (isOnline(context)) {
                    Log.d(TAG, "ACCESS INTERNET SUCCESS!");
//                    getDataFireBase();

                } else {
                    Log.d(TAG, "ACCESS INTERNET FAIL!");
//                    if (mGetReference != null && eventListener != null)
//                        mGetReference.removeEventListener(eventListener);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


    };

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getDataFireBase(){
        mDatabase = FirebaseDatabase.getInstance();
        mGetReference = mDatabase.getReference();
//        mGetReference.removeValue();
         eventListener = mGetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
                boolean conn = mqttConnInfo.getBoolean("connStatus", false);
                if (dataSnapshot.exists() && conn && isOnline(getApplicationContext())) {
                    model.deleteDatabase();
                    //Get entire data from realtime database
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    //Check all device ID from database
                    for (String deviceID : dataMap.keySet()) {
                        Log.d(TAG, "===========key: " + deviceID + "===========");
                        //Get all objects from device id
                        Object numDeviceData = dataMap.get(deviceID);
                        HashMap<String, Object> allDeviceData = (HashMap<String, Object>) numDeviceData;
                        //Check all key in each device id
                        for (String key : allDeviceData.keySet()){
                            Object data = allDeviceData.get(key);
                            HashMap<String, Object> sensor = (HashMap<String, Object>) data;
                            Log.d(TAG, "currentTime: " + sensor.get("time"));
                            Log.d(TAG, "bright: " + sensor.get("bright"));
                            Log.d(TAG, "temp: " + sensor.get("temp"));
                            Log.d(TAG, "humidity: " + sensor.get("humidity"));
                            Log.d(TAG, "-------------------------------------");
                            int id = Character.getNumericValue(deviceID.charAt(deviceID.length()-1));
                            temp = Integer.parseInt(sensor.get("temp")+"");
                            bright = Integer.parseInt(sensor.get("bright")+"");
                            humidity = Integer.parseInt(sensor.get("humidity")+"");
                            model.insert(new DeviceEntity((String)sensor.get("time"), id, temp, bright, humidity));
                        }
//
//                        try{
//
//                            String mString = String.valueOf(dataMap.get(key));
//                            Log.d(TAG, mString + "");
//
//
//                        }catch (ClassCastException cce2){
//
//                        }
                    }

                    mGetReference.removeEventListener(eventListener);
                }
                else
                    mGetReference.removeEventListener(eventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_item_menu) {
            dialogConnectSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Interact with setting MQTT connection dialog
     */
    private void dialogConnectSetting() {
        dialog_setting = new Dialog(this);
        dialog_setting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_setting.setContentView(R.layout.dialog_connection_setting);

        edt_hostname = (EditText) dialog_setting.findViewById(R.id.edt_hostname);
        edt_port = (EditText) dialog_setting.findViewById(R.id.edt_port);
        edt_username = (EditText) dialog_setting.findViewById(R.id.edt_username);
        edt_password = (EditText) dialog_setting.findViewById(R.id.edt_password);
        checkbox = (CheckBox) dialog_setting.findViewById(R.id.checkbox);
        btn_save = (Button) dialog_setting.findViewById(R.id.btn_save);
        btn_cancel = (Button) dialog_setting.findViewById(R.id.btn_cancel);

        //Restore data connection from share preferences
        restorePreference();

        //Set data for edit text and checkbox
        edt_hostname.setText(hostname);
        edt_port.setText(port);
        edt_username.setText(username);
        edt_password.setText(password);
        checkbox.setChecked(chkShowPassword);

        //Show and hide password
        if (chkShowPassword)
            edt_password.setTransformationMethod(null);
        else
            edt_password.setTransformationMethod(new PasswordTransformationMethod());

        //Set click on checkbox
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show and hide password
                if (checkbox.isChecked())
                    edt_password.setTransformationMethod(null);
                else
                    edt_password.setTransformationMethod(new PasswordTransformationMethod());

                edt_password.setSelection(edt_password.getText().length());
            }
        });

        //Set click on button Save
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostname = edt_hostname.getText().toString();
                port = edt_port.getText().toString();
                username = edt_username.getText().toString();
                password = edt_password.getText().toString();
                chkShowPassword = checkbox.isChecked();

                if (hostname.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter hostname", Toast.LENGTH_LONG).show();
                } else if (port.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter port", Toast.LENGTH_LONG).show();
                } else if (username.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_LONG).show();
                } else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
                } else {
                    savingPreferences();
                    dialog_setting.cancel();
                    dialogConnectAnimation();
                    startMQTT();
                }
            }
        });

        //Set click on button Cancel
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setting.cancel();
            }
        });

        dialog_setting.show();
    }

    /**
     * Create connect animation dialog
     */
    private void dialogConnectAnimation() {
        dialog_animation = new Dialog(this);
        dialog_animation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_animation.setContentView(R.layout.dialog_connect_animation);
        imgConnAnimation = dialog_animation.findViewById(R.id.img_connect_animation);
        imgConnAnimation.setBackgroundResource(R.drawable.animation_wifi);
        wifiAnimation = (AnimationDrawable) imgConnAnimation.getBackground();
        wifiAnimation.start();
        dialog_animation.show();
    }

    /**
     * Save data into share preferences
     */
    private void savingPreferences() {
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        SharedPreferences.Editor saveData = mqttConnInfo.edit();
        saveData.putString("MQTT_Hostname", hostname);
        saveData.putString("port", port);
        saveData.putString("username", username);
        saveData.putString("password", password);
        saveData.putBoolean("show_password", chkShowPassword);
        saveData.commit();
    }

    /**
     * Restore data from share preference
     */
    private void restorePreference() {
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        hostname = mqttConnInfo.getString("MQTT_Hostname", "");
        port = mqttConnInfo.getString("port", "");
        username = mqttConnInfo.getString("username", "");
        password = mqttConnInfo.getString("password", "");
        chkShowPassword = mqttConnInfo.getBoolean("show_password", false);
    }

    /**
     * Start MQTT connection
     */
    private void startMQTT() {

        //To avoid creating many thread mqtt conflict together
        //Disconnect the previous MQTT client to get new connection
        if (mqttAndroidClient != null) {
            try {
                mqttApi.disconnect(mqttAndroidClient);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        //Create viewmodel object
        model = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        String hostserver = "tcp://" + hostname + ":" + port;
        mqttApi = new MqttApi(getApplicationContext(), hostserver, username, password);
        mqttApi.setDialog(dialog_animation);
        mqttApi.setViewModel(model);
        mqttApi.connect();

        mqttAndroidClient = mqttApi.getMqttAndroidClient();

        mqttApi.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                boolean isConnected = mqttAndroidClient.isConnected();
                //Save connect status to share preference
                getApplicationContext().getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE)
                        .edit()
                        .putBoolean("connStatus", isConnected)
                        .commit();
                //Save connect status to viewmodels
                model.setConnStatus(isConnected);

                getDataFireBase();
                //Publish event
                if (isRunning != true) {
                    EventBus.getDefault().post(new ConnectStatusEvent(isConnected));
                }

                Toast.makeText(getApplicationContext(), "Connected MQTT successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void connectionLost(Throwable cause) {
                boolean isConnected = mqttAndroidClient.isConnected();
                //Save connect status
                getApplicationContext().getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE)
                        .edit()
                        .putBoolean("connStatus", isConnected)
                        .commit();
                //Save connect status to viewmodels
                model.setConnStatus(isConnected);

                //Publish event
                if (isRunning != true) {
                    EventBus.getDefault().post(new ConnectStatusEvent(isConnected));
                }

                Toast.makeText(getApplicationContext(), "MQTT connection failed!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, topic);

                switch (topic){
                    case "device/data":
//                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        currentTime = df.format(calendar.getInstance().getTime());
                        parseJsonData(message.toString());
                        savingDatabase();
                        break;
                    case "gw/gps":
                        parseJsonLocate(message.toString());
                        EventBus.getDefault().post(new GPSLocateEvent(lat,lng));
                        //Save position value to share preference
                        getApplicationContext().getSharedPreferences("GPS_LOCATE", MODE_PRIVATE)
                                .edit()
                                .putLong("GPS_Lat", Double.doubleToRawLongBits(lat))
                                .putLong("GPS_Long", Double.doubleToRawLongBits(lng))
                                .commit();
                        break;
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    /**
     * Parse JSON object with data value
     * @return
     */
    private void parseJsonData(String mess) throws JSONException {
        JSONObject parse = new JSONObject(mess);
        deviceID = parse.getInt("device_ID");
        currentTime = parse.getString("cur_time");
        JSONObject param = parse.getJSONObject("param");
        temp = param.getInt("sensor_temp");
        bright = param.getInt("sensor_bright");
        humidity = param.getInt("sensor_humidity");
    }

    /**
     *  Parse JSON object with position value
     */
    private void parseJsonLocate(String pos) throws JSONException {
        JSONObject parse = new JSONObject(pos);
        JSONObject param = parse.getJSONObject("gps");
        lat = param.getDouble("gps_lat");
        lng = param.getDouble("gps_long");
        Log.d(TAG, lat + "");
        Log.d(TAG, lng + "");

    }

    /**
     * Save into database via repository
     * @return
     */
    private void savingDatabase(){
        model.insert(new DeviceEntity(currentTime, deviceID, temp, bright, humidity));
    }

    //Get previously created object
    public static MqttApi getMqttApi() {
        return mqttApi;
    }
}



