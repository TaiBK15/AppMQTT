package com.example.mqttapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mqttapplication.R;

public class FragmentDeviceList extends Fragment{

//    AnimationDrawable wifiAnimation;
//    private ImageView imgView;

    private LinearLayout device_1, device_2, device_3, device_4,
                         device_5, device_6, device_7, device_8;

    public FragmentDeviceList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        //Define linear layout with id
        device_1 = (LinearLayout) view.findViewById(R.id.ln_device_1);
        device_2 = (LinearLayout) view.findViewById(R.id.ln_device_2);
        device_3 = (LinearLayout) view.findViewById(R.id.ln_device_3);
        device_4 = (LinearLayout) view.findViewById(R.id.ln_device_4);
        device_5 = (LinearLayout) view.findViewById(R.id.ln_device_5);
        device_6 = (LinearLayout) view.findViewById(R.id.ln_device_6);
        device_7 = (LinearLayout) view.findViewById(R.id.ln_device_7);
        device_8 = (LinearLayout) view.findViewById(R.id.ln_device_8);

        //Set on click on linear layout
        device_1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 1", Toast.LENGTH_SHORT).show();
            }
        });

        device_2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 2", Toast.LENGTH_SHORT).show();
            }
        });

        device_3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 3", Toast.LENGTH_SHORT).show();
            }
        });

        device_4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 4", Toast.LENGTH_SHORT).show();
            }
        });

        device_5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 5", Toast.LENGTH_SHORT).show();
            }
        });

        device_5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 5", Toast.LENGTH_SHORT).show();
            }
        });

        device_6.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 5", Toast.LENGTH_SHORT).show();
            }
        });

        device_7.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 5", Toast.LENGTH_SHORT).show();
            }
        });

        device_8.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on device 5", Toast.LENGTH_SHORT).show();
            }
        });
//        imgView = (ImageView) view.findViewById(R.id.image_test);
//        imgView.setBackgroundResource(R.drawable.animation_wifi);
//        wifiAnimation = (AnimationDrawable) imgView.getBackground();
//        wifiAnimation.start();

        return view;
    }
}
