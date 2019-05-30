package com.example.mqttapplication.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mqttapplication.R;
import com.example.mqttapplication.activity.LoginActivity;
import com.example.mqttapplication.activity.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final String TAG = "Firebase message";
    public static String TOPIC_FCM = "LORA_SYSTEM";

    private String title, body;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Data message" + remoteMessage.getData());
        }

        try {
            parseJSONDataPayload(remoteMessage.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refresh token" + s);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_FCM).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "subscribe to " + TOPIC_FCM + " successfully");
            }
        });
    }

    private void parseJSONDataPayload(Map<String, String> data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        title = jsonObject.getString("title");
        body = jsonObject.getString("body");
        sendNotification(title, body);

    }

    /**
     * Send notification to android device
     * @param title
     * @param body
     */
    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_bk_small)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
