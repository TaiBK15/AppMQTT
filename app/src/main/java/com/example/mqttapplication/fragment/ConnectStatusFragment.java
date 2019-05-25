package com.example.mqttapplication.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mqttapplication.R;
import com.example.mqttapplication.viewmodel.MainActivityViewModel;

import static android.content.Context.MODE_PRIVATE;

public class ConnectStatusFragment extends Fragment {

    private ImageView imgViewConnStatus;
    private boolean connStatus;

    public ConnectStatusFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_status, container, false);
        imgViewConnStatus = view.findViewById(R.id.img_connection_status);

        //Get data from Share Preerences
        SharedPreferences mqttConnInfo = getActivity().getSharedPreferences("MQTTConnectionSetup", MODE_PRIVATE);
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

        MainActivityViewModel model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class );
        model.getConnStatus().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
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
        return view;
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
