package com.example.mqttapplication.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mqttapplication.R;

public class FragmentDeviceList extends Fragment{

    AnimationDrawable wifiAnimation;
    private ImageView imgView;

    public FragmentDeviceList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list , container, false);


//        imgView = (ImageView) view.findViewById(R.id.image_test);
//        imgView.setBackgroundResource(R.drawable.animation_wifi);
//        wifiAnimation = (AnimationDrawable) imgView.getBackground();
//        wifiAnimation.start();

        return view;
    }
}
