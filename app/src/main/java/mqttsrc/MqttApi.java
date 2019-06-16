package mqttsrc;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.mqttapplication.viewmodel.MainActivityViewModel;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import static android.content.Context.MODE_PRIVATE;

public class MqttApi {

    final String TAG = "MQTT API";
    final String clientID = Build.ID;
    private Context context;
    private String hostserver, username, password;
    private boolean isConnected;

    private MqttAndroidClient mqttAndroidClient;

    private Dialog dialog;
    private MainActivityViewModel fragConnModel;

    public MqttAndroidClient getMqttAndroidClient() {
        return this.mqttAndroidClient;
    }

    public void setViewModel(MainActivityViewModel fragConnModel) {
        this.fragConnModel = fragConnModel;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
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

                    if(dialog != null)
                        dialog.cancel();

                    //Subscribe only 1 topic
                    subscribeToTopic("device/data", 0);
                    subscribeToTopic("device/online", 0);
                    subscribeToTopic("gw/gps", 0);
                    subscribeToTopic("device/sw_ack/id_1", 0);
                    subscribeToTopic("device/sw_ack/id_2", 0);
                    subscribeToTopic("device/sw_ack/id_3", 0);
                    subscribeToTopic("device/sw_ack/id_4", 0);
                    subscribeToTopic("device/sw_ack/id_5", 0);
                    subscribeToTopic("device/sw_ack/id_6", 0);
                    subscribeToTopic("device/sw_ack/id_7", 0);
                    subscribeToTopic("device/sw_ack/id_8", 0);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Connected MQTT failed!");
                    Toast.makeText(context, "Connected MQTT failed", Toast.LENGTH_LONG).show();

                    if(dialog != null)
                        dialog.cancel();

                    isConnected = mqttAndroidClient.isConnected();
                    //Save connect status into share preference
                    context.getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE)
                            .edit()
                            .putBoolean("connStatus", isConnected)
                            .commit();
                    //Save connect status into viewmodel
                    fragConnModel.setConnStatus(isConnected);
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
            message.setRetained(false);
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
