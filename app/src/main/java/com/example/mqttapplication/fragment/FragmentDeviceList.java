package com.example.mqttapplication.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.example.mqttapplication.activity.DeviceDetailActivity;
import com.example.mqttapplication.activity.DeviceLogActivity;
import com.example.mqttapplication.activity.MainActivity;
import com.example.mqttapplication.repository.DeviceRepository;
import com.example.mqttapplication.roomdatabase.DeviceEntity;
import com.example.mqttapplication.viewmodel.MainActivityViewModel;

public class FragmentDeviceList extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private String hostname, deviceName;
    private int deviceId;
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
        device_1.setOnClickListener(this);
        device_2.setOnClickListener(this);
        device_3.setOnClickListener(this);
        device_4.setOnClickListener(this);
        device_5.setOnClickListener(this);
        device_6.setOnClickListener(this);
        device_7.setOnClickListener(this);
        device_8.setOnClickListener(this);

        //Set on long click on linear layout
        device_1.setOnLongClickListener(this);
        device_2.setOnLongClickListener(this);
        device_3.setOnLongClickListener(this);
        device_4.setOnLongClickListener(this);
        device_5.setOnLongClickListener(this);
        device_6.setOnLongClickListener(this);
        device_7.setOnLongClickListener(this);
        device_8.setOnLongClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
        switch (v.getId()){
            case R.id.ln_device_1:
                intent.putExtra("Title", "DEVICE 1");
                intent.putExtra("BkgToolbar",  1);
                break;
            case R.id.ln_device_2:
                intent.putExtra("Title", "DEVICE 2");
                intent.putExtra("BkgToolbar",  2);
                break;
            case R.id.ln_device_3:
                intent.putExtra("Title", "DEVICE 3");
                intent.putExtra("BkgToolbar",  3);
                break;
            case R.id.ln_device_4:
                intent.putExtra("Title", "DEVICE 4");
                intent.putExtra("BkgToolbar",  4);
                break;
            case R.id.ln_device_5:
                intent.putExtra("Title", "DEVICE 5");
                intent.putExtra("BkgToolbar",  5);
                break;
            case R.id.ln_device_6:
                intent.putExtra("Title", "DEVICE 6");
                intent.putExtra("BkgToolbar",  6);
                break;
            case R.id.ln_device_7:
                intent.putExtra("Title", "DEVICE 7");
                intent.putExtra("BkgToolbar",  7);
                break;
            case R.id.ln_device_8:
                intent.putExtra("Title", "DEVICE 8");
                intent.putExtra("BkgToolbar",  8);
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()){
            case R.id.ln_device_1:
                deviceName = "DEVICE 1";
                deviceId = 1;
                break;
            case R.id.ln_device_2:
                deviceName = "DEVICE 2";
                deviceId = 2;
                break;
            case R.id.ln_device_3:
                deviceName = "DEVICE 3";
                deviceId = 3;
                break;
            case R.id.ln_device_4:
                deviceName = "DEVICE 4";
                deviceId = 4;
                break;
            case R.id.ln_device_5:
                deviceName = "DEVICE 5";
                deviceId = 5;
                break;
            case R.id.ln_device_6:
                deviceName = "DEVICE 6";
                deviceId = 6;
                break;
            case R.id.ln_device_7:
                deviceName = "DEVICE 7";
                deviceId = 7;
                break;
            case R.id.ln_device_8:
                deviceName = "DEVICE 8";
                deviceId = 8;
                break;
        }

        final PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_device,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.popup_log:
                            Intent intent = new Intent(getContext(), DeviceLogActivity.class);
                            intent.putExtra("Title", deviceName);
                            intent.putExtra("BkgToolbar",  deviceId);
                            startActivity(intent);
                        return true;
                    case R.id.popup_reset:
                        Toast.makeText(getContext(), "Reset data in device " + deviceId, Toast.LENGTH_LONG).show();
                        MainActivityViewModel model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class );
                        model.deleteAll(deviceId);
                }
                return true;
            }
        });

        popupMenu.show();

        return true;
    }
}
