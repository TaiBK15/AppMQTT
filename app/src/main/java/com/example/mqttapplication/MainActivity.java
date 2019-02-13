package com.example.mqttapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import mqttsrc.MqttApi;

public class MainActivity extends AppCompatActivity implements MqttApi.Isconnect {
//    public MqttAndroidClient mqttAndroidClient;
//
//    final String hostserver = "tcp://m10.cloudmqtt.com:10452";
//    final String clientID = "mqtttest";
//    final String username = "asxdszmm";
//    final String password = "NJw3kQLh_Mze";
//    final String publish_topic = "DOWNLINK";
//    final String subcribe_topic = "UPLINK";

    MqttApi mqttclient;
    TextView datarecv;
    Button conn, disconn, send;
    private boolean flag_conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datarecv = findViewById(R.id.message);
        send = findViewById(R.id.send);
        conn = findViewById(R.id.connect);
        disconn = findViewById(R.id.disconnect);

        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMqtt();
            }
        });

//        disconn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mqttclient.disconnect();
//            }
//        });


    }

    private void startMqtt() {
//        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), hostserver, clientID);
        mqttclient = new MqttApi(getApplicationContext());
        mqttclient.connect();
//        mqttclient.publishMessage(mqttAndroidClient, clientID, 0, publish_topic);

//        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();

        mqttclient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                datarecv.setText(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
//        if(flag_conn == true)
//            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(this, "fail to connect", Toast.LENGTH_LONG).show();
    }


    @Override
    public void inform(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }

}
