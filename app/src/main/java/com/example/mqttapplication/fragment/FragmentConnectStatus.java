package com.example.mqttapplication.fragment;

import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class FragmentConnectStatus extends Fragment {

    private ImageView imgViewConnStatus;
    private Thread waitConnect;
    private boolean connStatus;

    public FragmentConnectStatus() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_status, container, false);
        imgViewConnStatus = view.findViewById(R.id.img_connection_status);

        waitConnect = new Thread() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                //Get connect status from Share Preferences
                                SharedPreferences mqttConnInfo = getActivity().getSharedPreferences("MQTTConnectionSetup",
                                        MODE_PRIVATE);
                                connStatus = mqttConnInfo.getBoolean("connStatus", false);
                                if (connStatus) {
                                    //Set icon connected
                                    imgViewConnStatus.setImageResource(R.drawable.ic_connected);
                                    imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_connected);
                                } else {
                                    //Set icon disconnected
                                    imgViewConnStatus.setImageResource(R.drawable.ic_disconnected);
                                    imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_disconnected);
                                }
                            }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        waitConnect.start();
        return view;

//
//                    while(timer <= timeCountSecond){
//                        try{
//                            Thread.sleep(1000);
//                        }catch (InterruptedException ex){
//                            ex.printStackTrace();
//                        }
//                        timer++;
//                        Log.d("Thread Create", "Running");
//                    }
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if(connStatus){
//                                //Set icon connected
//                                imgViewConnStatus.setImageResource(R.drawable.ic_connected);
//                                imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_connected);
//                            }
//                            else{
//                                //Set icon disconnected
//                                imgViewConnStatus.setImageResource(R.drawable.ic_disconnected);
//                                imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_disconnected);
//                            }
//
//                        }
//                    });
//
//                } catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
