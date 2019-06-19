package com.example.mqttapplication.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.TextView;

import com.example.mqttapplication.R;
import com.example.mqttapplication.eventbus.ConnectStatusEvent;
import com.example.mqttapplication.model.MyMarkerData;
import com.example.mqttapplication.viewmodel.MainActivityViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    final String TAG = "MapFragment";
    private double lat_gw, lng_gw;
    private String connectedDevice;
    private ArrayList<MyMarkerData> deviceMap;
    private SupportMapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SharedPreferences gps_locate = getActivity().getSharedPreferences("GPS_LOCATE", MODE_PRIVATE);
        lat_gw = Double.longBitsToDouble(gps_locate.getLong("GPS_Lat", 0));
        lng_gw = Double.longBitsToDouble(gps_locate.getLong("GPS_Long", 0));
        connectedDevice = gps_locate.getString("ConnectedDevice", "");
        // Getting Reference to SupportMapFragment of fragment_map.xml
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        MainActivityViewModel model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class );
        model.getLatlng().observe(getActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(@Nullable final LatLng latLng) {
                Log.d(TAG, "GPS came");

                // Getting reference to google map
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        deviceMap = getDevicePosition(latLng, connectedDevice);
                        drawGateway(googleMap, latLng);
                        drawCircle(googleMap, latLng);
                        if(deviceMap != null)
                            drawDevice(googleMap, deviceMap);

                            // Creating CameraUpdate object for position
                        CameraUpdate updatePosition = CameraUpdateFactory.newLatLngZoom(latLng, 17);

                        //Update position with animation
                        googleMap.animateCamera(updatePosition);
                    }
                });
            }
        });

        model.getOnlineDevice().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                SharedPreferences gps_locate = getActivity().getSharedPreferences("GPS_LOCATE", MODE_PRIVATE);
                lat_gw = Double.longBitsToDouble(gps_locate.getLong("GPS_Lat", 0));
                lng_gw = Double.longBitsToDouble(gps_locate.getLong("GPS_Long", 0));
                connectedDevice = s;
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng gwCenter = new LatLng(lat_gw, lng_gw);
                        deviceMap = getDevicePosition(gwCenter, connectedDevice);
                        drawGateway(googleMap, gwCenter);
                        drawCircle(googleMap, gwCenter);
                        if(deviceMap != null){
                            drawDevice(googleMap, deviceMap);

                        // Creating CameraUpdate object for position
                        CameraUpdate updatePosition = CameraUpdateFactory.newLatLngZoom(gwCenter, 17);

                        //Update position with animation
                        googleMap.animateCamera(updatePosition);
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng gwCenter = new LatLng(lat_gw, lng_gw);
        deviceMap = getDevicePosition(gwCenter, connectedDevice);
        drawGateway(googleMap, gwCenter);
        drawCircle(googleMap, gwCenter);
        if(deviceMap != null)
            drawDevice(googleMap, deviceMap);

        // Creating CameraUpdate object for position
        CameraUpdate updatePosition = CameraUpdateFactory.newLatLngZoom(gwCenter, 17);

        //Update position with animation
        googleMap.animateCamera(updatePosition);

    }

    private void drawGateway(GoogleMap googleMap, LatLng gwPoint){
        MarkerOptions options = new MarkerOptions();

        options.position(gwPoint);

        options.title("Gateway");

        options.snippet("Latitude:" + gwPoint.latitude + ",Longitude:" + gwPoint.longitude);

        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gateway));

        // Adding Marker on the Google Map
        googleMap.clear();
        googleMap.addMarker(options);
    }

    private void drawDevice(GoogleMap googleMap, ArrayList<MyMarkerData> dataMarker){
        for (MyMarkerData object :dataMarker){
             googleMap.addMarker(new MarkerOptions()
                    .position(object.getLatLng())
                    .icon(BitmapDescriptorFactory.fromBitmap(object.getBitmap()))
            );
        }
    }

    private void drawCircle(GoogleMap googleMap, LatLng center){
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(center);

        // Radius of the circle
        circleOptions.radius(10);

        // Fill color of the circle
        circleOptions.fillColor(0xAAFF3229);

        // Border width of the circle
        circleOptions.strokeWidth(0);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);
    }

    private ArrayList<MyMarkerData> getDevicePosition(LatLng center, String seq){
        if(seq.length() != 0){
            double deg = 360/(seq.length());
            ArrayList<MyMarkerData> dataMap = new ArrayList<>();
            for (int i = 1; i<=seq.length(); i++){
                double latDevie = Math.sin(i * deg * Math.PI / 180)*0.001 + center.latitude;
                double lngDevie = Math.cos(i * deg * Math.PI / 180)*0.001 + center.longitude;
                dataMap.add(new MyMarkerData(new LatLng(latDevie, lngDevie), "device_" + seq.charAt(i-1), createBitmapMarker("device_" + seq.charAt(i-1))));
            }
            return dataMap;
        }
        else
            return null;

    }

    private Bitmap createBitmapMarker(String title){
        LinearLayout tv = (LinearLayout) this.getLayoutInflater().inflate(R.layout.bitmap_marker, null, false);
        TextView tvTitle = (TextView) tv.findViewById(R.id.tvTitleMarker);
        tvTitle.setText(title);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
        tv.setDrawingCacheEnabled(true);
        tv.buildDrawingCache();
        Bitmap bm = tv.getDrawingCache();
        return bm;
    }
}


