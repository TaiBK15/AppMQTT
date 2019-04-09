package com.example.mqttapplication.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.example.mqttapplication.adapter.ViewPagerAdapter;
import com.example.mqttapplication.fragment.FragmentConnectStatus;
import com.example.mqttapplication.fragment.FragmentDeviceList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private EditText edt_hostname, edt_port, edt_username, edt_password;
    private Button btn_save, btn_cancel;
    private CheckBox checkbox;
    private String hostname, port, username, password;
    private boolean chkShowPassword;


//    public MqttAndroidClient mqttAndroidClient;
//
//    final String hostserver = "tcp://m10.cloudmqtt.com:10452";
//    final String clientID = "mqtttest";
//    final String username = "asxdszmm";
//    final String password = "NJw3kQLh_Mze";
//    final String publish_topic = "DOWNLINK";
//    final String subcribe_topic = "UPLINK";

//    boolean flag_conn;
//
//    MqttApi mqttclient;
//    TextView datarecv;
//    Button conn, disconn, send;
//
//    public MainActivity() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_mqtt_app_small);

        tablayout = (TabLayout) findViewById(R.id.tablayout_main_activity);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add fragment
        adapter.addFragment(new FragmentConnectStatus(), "Connect");
        adapter.addFragment(new FragmentDeviceList(), "Device");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        //Add icon for tablayout
        tablayout.getTabAt(0).setIcon(R.drawable.ic_wifi);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_list);



    }

//    private void startMqtt() {
////        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), hostserver, clientID);
//        mqttclient = new MqttApi(getApplicationContext());
//        mqttclient.isconn = this;
//        mqttclient.connect();
////        mqttclient.publishMessage(mqttAndroidClient, clientID, 0, publish_topic);
//
////        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
//
//        mqttclient.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//
//            }
//
//            @Override
//            public void connectionLost(Throwable cause) {
//
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) {
//                datarecv.setText(message.toString());
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });
////        if(flag_conn == true)
////            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
////        else
////            Toast.makeText(this, "fail to connect", Toast.LENGTH_LONG).show();
//    }
//
//
//    @Override
//    public void inform(String mess) {
//        Toast.makeText(MainActivity.this, mess, Toast.LENGTH_LONG).show();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.main_item_menu){
            dialogConnectSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * Interact with setting MQTT connection dialog
     */
    private void dialogConnectSetting(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_connetion_setting);

        edt_hostname = (EditText) dialog.findViewById(R.id.edt_hostname);
        edt_port = (EditText) dialog.findViewById(R.id.edt_port);
        edt_username = (EditText) dialog.findViewById(R.id.edt_username);
        edt_password = (EditText) dialog.findViewById(R.id.edt_password);
        checkbox = (CheckBox) dialog.findViewById(R.id.checkbox);
        btn_save = (Button) dialog.findViewById(R.id.btn_save);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        //Restore data connection from share preferences
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        hostname = mqttConnInfo.getString("MQTT_Hostname", "");
        port = mqttConnInfo.getString("port", "");
        username = mqttConnInfo.getString("username", "");
        password = mqttConnInfo.getString("password", "");
        chkShowPassword = mqttConnInfo.getBoolean("show_password", false);

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
                savingPreferences();
                dialog.cancel();
            }
        });

        //Set click on button Cancel
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * Save data into share preferences
     */
    private void savingPreferences(){
        SharedPreferences mqttConnInfo = getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
        SharedPreferences.Editor saveData = mqttConnInfo.edit();
        saveData.putString("MQTT_Hostname", hostname);
        saveData.putString("port", port);
        saveData.putString("username",username);
        saveData.putString("password", password);
        saveData.putBoolean("show_password", chkShowPassword);
        saveData.commit();
    }

    public String getHostname() {
        return hostname;
    }
}



