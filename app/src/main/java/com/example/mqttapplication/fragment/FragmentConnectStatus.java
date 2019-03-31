package com.example.mqttapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mqttapplication.R;

public class FragmentConnectStatus extends Fragment {
    private ImageView imgViewConnStatus;

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

        //Set icon disconnected
        imgViewConnStatus.setImageResource(R.drawable.ic_disconnected);
        imgViewConnStatus.setBackgroundResource(R.drawable.bkg_ic_disconnected);

        return view;
    }
}
