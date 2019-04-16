package mqttsrc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.mqttapplication.activity.MainActivity;

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

import static android.content.Context.MODE_PRIVATE;

public class MqttApi {

    final String TAG = "MQTT API";
    final String clientID = "APP MQTT";
    private Context context;
    private String hostserver, username, password;
    private String publish_topic, subscribe_topic;
    private MqttAndroidClient mqttAndroidClient;
    private boolean isConnected;

    public MqttAndroidClient getMqttAndroidClient() {
        return this.mqttAndroidClient;
    }

    public MqttApi(Context context, String hostserver, String username, String password) {
        this.context = context;
        this.hostserver = hostserver;
        this.username = username;
        this.password = password;

        //Create new object mqttclient
        mqttAndroidClient = new MqttAndroidClient(context, hostserver, clientID);
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect() {

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(false);
        mqttConnectOptions.setCleanSession(false); //default false
        mqttConnectOptions.setConnectionTimeout(1); //default 30s
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

                    Log.d(TAG, "Connected MQTT successfully");

                    //Subscribe on 8 topics
                    subscribeToTopic("device_1/data", 0);
                    subscribeToTopic("device_2/data", 0);
                    subscribeToTopic("device_3/data", 0);
                    subscribeToTopic("device_4/data", 0);
                    subscribeToTopic("device_5/data", 0);
                    subscribeToTopic("device_6/data", 0);
                    subscribeToTopic("device_7/data", 0);
                    subscribeToTopic("device_8/data", 0);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Connected MQTT failed");
                    Toast.makeText(context, "Connected MQTT failed!", Toast.LENGTH_LONG).show();

                    isConnected = mqttAndroidClient.isConnected();
                    //Save connect status
                    context.getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE)
                            .edit()
                            .putBoolean("connStatus", isConnected)
                            .commit();
                }
            });

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    private void publish(@NonNull MqttAndroidClient client,
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

    private void subscribe(@NonNull MqttAndroidClient client,
                          @NonNull final String topic, int qos) {
        try {
            IMqttToken token = client.subscribe(topic, qos);
            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

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
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
            }
        });
    }

    public void disconnect(@NonNull MqttAndroidClient client)
            throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        client.unregisterResources();
        client = null;
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Disconnect successfully");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
            }
        });
    }

    /**
     * Sumup: Rewrite method publish to topic
     */
    public void publishToTopic(String mess, int qos, String topic){
        publish(mqttAndroidClient, mess, qos, topic);
    }

    /**
     * Sumup: Rewrite method subscribe to topic
     */
    public void subscribeToTopic(String topic, int qos){
        subscribe(mqttAndroidClient, topic, qos);
    }

}
