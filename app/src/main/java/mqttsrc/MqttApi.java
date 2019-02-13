package mqttsrc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MqttApi {

    final String hostserver = "tcp://m10.cloudmqtt.com:10452";
    final String clientID = "mqtttest";
    final String username = "asxdszmm";
    final String password = "NJw3kQLh_Mze";
    final String publish_topic = "DOWNLINK";
    final String subcribe_topic = "UPLINK";
    public MqttAndroidClient mqttAndroidClient;
    public boolean flag = true;
    private String Tag = "MqttApplication";
    public Isconnect isconn;

    public MqttApi(Context context) {
        mqttAndroidClient = new MqttAndroidClient(context, hostserver, clientID);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect() {

        final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        Log.d(Tag, "Wait to process 1");

        try {
            Log.d(Tag, "Wait to process 2");

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
//                    publishMessage(mqttAndroidClient, "TestMQTT", 0, publish_topic);
//                    subscribe(mqttAndroidClient, subcribe_topic, 0);
                    sendMessage();
                    Log.d(Tag, "Connected");
                    try {
                        isconn.inform("Connected");

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Toast.makeText(this, "failed to connect", Toast.LENGTH_LONG).show();
                    Log.d(Tag, "Fail to connect");
                    try {
                        isconn.inform("Fail to connect. Check your network");

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
//                    return false;
                }

            });
            Log.d(Tag, "Wait to process 3");

        } catch (MqttException ex) {
            ex.printStackTrace();
        }

        Log.d(Tag, "connected 2");
//        if (flag == true) {
//            Log.d(Tag, "connected 2");
//            return true;
//        }
//        else
//        {
//            Log.d(Tag, "Fail to connect");
//            return false;
//        }
    }

    public void sendMessage() {
        publishMessage(mqttAndroidClient, "TestMQTT", 0, publish_topic);

    }
    public void publishMessage(@NonNull MqttAndroidClient client,
                               @NonNull String msg, int qos, @NonNull String topic) {
        try {
            byte[] encodedPayload = new byte[0];
            encodedPayload = msg.getBytes("UTF_8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setId(5866);
            message.setRetained(true);
            message.setQos(qos);
            client.publish(topic, message);
        } catch (MqttException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

    }

    public void subscribe(@NonNull MqttAndroidClient client,
                          @NonNull final String topic, int qos) {
        try {
            IMqttToken token = client.subscribe(topic, qos);
            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
//              Toast.makeText(this, "Subscribe successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                Toast.makeText(this, "Subscribe fail", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void unSubscribe(@NonNull MqttAndroidClient client,
                            @NonNull final String topic) throws MqttException {

        IMqttToken token = client.unsubscribe(topic);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
//                Toast.makeText(this, "Unsubscribe successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                Toast.makeText(this, "Unsubscribe fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void disconnect(@NonNull MqttAndroidClient client)
            throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
//                Toast.makeText(this, "Disconnect successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
//                Toast.makeText(this, "Disconnect fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface Isconnect {
        void inform(String mess);
    }

}
