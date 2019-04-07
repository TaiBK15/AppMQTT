package com.example.mqttapplication.fragment;

import android.content.Intent;
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
import com.example.mqttapplication.activity.DeviceDetailActivity;

public class FragmentDeviceList extends Fragment{

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
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 1");
                intent.putExtra("BkgToolbar",  1);
                startActivity(intent);
            }
        });

        device_2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 2");
                intent.putExtra("BkgToolbar",  2);
                startActivity(intent);
            }
        });

        device_3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 3");
                intent.putExtra("BkgToolbar",  3);
                startActivity(intent);
            }
        });

        device_4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 4");
                intent.putExtra("BkgToolbar",  4);
                startActivity(intent);
            }
        });

        device_5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 5");
                intent.putExtra("BkgToolbar",  5);
                startActivity(intent);
            }
        });

        device_6.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 6");
                intent.putExtra("BkgToolbar",  6);
                startActivity(intent);
            }
        });

        device_7.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 7");
                intent.putExtra("BkgToolbar",  7);
                startActivity(intent);
            }
        });

        device_8.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                intent.putExtra("Title", "DEVICE 8");
                intent.putExtra("BkgToolbar",  8);
                startActivity(intent);
            }
        });


        return view;
    }
}
