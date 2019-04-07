package com.example.mqttapplication.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mqttapplication.R;

public class FragmentConnectStatus extends Fragment {
    private ImageView imgViewConnStatus;
    AnimationDrawable wifiAnimation;
    int timeCountSecond = 5;
    Thread waitConnect;

    public FragmentConnectStatus() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_status, container, false);
        imgViewConnStatus = (ImageView) view.findViewById(R.id.img_connection_status);

        //Set icon connected
//        imgViewConnStatus.setImageResource(R.drawable.ic_connected);
//        imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_connected);

        waitConnect = new Thread(){
            int timer = 0;
            @Override
            public void run() {
                try{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imgViewConnStatus.setBackgroundResource(R.drawable.animation_wifi);
                            wifiAnimation = (AnimationDrawable) imgViewConnStatus.getBackground();
                            wifiAnimation.start();
                        }
                    });

                    while(timer <= timeCountSecond){
                        Thread.sleep(1000);
                        timer++;
                        Log.d("Thread Create", "Running");
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Set icon disconnected
                            imgViewConnStatus.setImageResource(R.drawable.ic_disconnected);
                            imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_disconnected);
                        }
                    });
//                    wifiAnimation.stop();



                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        waitConnect.start();



        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        waitConnect.interrupt();

    }
}
