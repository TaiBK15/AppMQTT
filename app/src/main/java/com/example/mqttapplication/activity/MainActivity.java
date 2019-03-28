package com.example.mqttapplication.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.mqttapplication.R;
import com.example.mqttapplication.adapter.ViewPagerAdapter;
import com.example.mqttapplication.fragment.FragmentConnectStatus;
import com.example.mqttapplication.fragment.FragmentDeviceList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

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

        tablayout = (TabLayout) findViewById(R.id.tablayout_main_activity);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add fragment
        adapter.addFragment(new FragmentConnectStatus(), "Connect");
        adapter.addFragment(new FragmentDeviceList(), "Device");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        //Add icon for tablayout
        tablayout.getTabAt(0).setIcon(R.drawable.ic_wifi_white);
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
}



