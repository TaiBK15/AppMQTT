package com.example.mqttapplication.services;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final String TAG = "Firebase message";
    private String TOPIC_FCM = "LORA_SYSTEM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, remoteMessage.getData().toString() + "get from" + remoteMessage.getFrom().toString());
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
}
