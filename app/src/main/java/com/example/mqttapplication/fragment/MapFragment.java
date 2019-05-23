package com.example.mqttapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mqttapplication.R;
import com.example.mqttapplication.eventbus.ConnectStatusEvent;
import com.example.mqttapplication.eventbus.GPSLocateEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.MODE_PRIVATE;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    final String TAG = "MapFragment";
    private double lat, lng;
    private SupportMapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SharedPreferences gps_locate = getActivity().getSharedPreferences("GPS_LOCATE", MODE_PRIVATE);
        lat = Double.longBitsToDouble(gps_locate.getLong("GPS_Lat", 0));
        lng = Double.longBitsToDouble(gps_locate.getLong("GPS_Long", 0));

        // Getting Reference to SupportMapFragment of fragment_map.xml
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        // Getting reference to google map
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void onEvent(GPSLocateEvent gpsLocateEvent){
        lat = gpsLocateEvent.getLat();
        lng = gpsLocateEvent.getLng();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(lat, lng);

        MarkerOptions options = new MarkerOptions();

        options.position(position);

        options.title("Gateway");

        options.snippet("Latitude:"+lat+",Longitude:"+lng);
        // Adding Marker on the Google Map
        googleMap.clear();
        googleMap.addMarker(options);

        // Creating CameraUpdate object for position
        CameraUpdate updatePosition = CameraUpdateFactory.newLatLngZoom(position, 17);

        //Update position with animation
        googleMap.animateCamera(updatePosition);

        // Updating the camera position to the user input latitude and longitude
//        googleMap.moveCamera(updatePosition);

    }
}
