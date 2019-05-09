package com.example.mqttapplication.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mqttapplication.R;
import com.example.mqttapplication.eventbus.ConnectStatusEvent;
import com.example.mqttapplication.repository.DeviceRepository;
import com.example.mqttapplication.roomdatabase.DeviceEntity;
import com.example.mqttapplication.viewmodel.DeviceDetailViewModel;
import com.example.mqttapplication.viewmodel.DeviceLogViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartFragment extends Fragment {
    final String TAG = "Fragment Chart";
    private int deviceID, typeChart;
    private final int TEMP = 0;
    private final int BRI = 1;
    private final int HUM = 2;
    private List yAxisValues;

    private Button btn_reset;
    private TextView tvNameChart;
    private LineChartView lineChartView;
    private LineChartData data;
    private Viewport viewport;
    private Line line;
    private List lines;

    private DeviceLogViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart, container, false);
        Bundle bundle = this.getArguments();
        deviceID = bundle.getInt("deviceID");
        typeChart =  bundle.getInt("typeChart");

        btn_reset = view.findViewById(R.id.btn_reset);
        tvNameChart = view.findViewById(R.id.tvNameChart);
        lineChartView = view.findViewById(R.id.chart);

        yAxisValues = new ArrayList();

        switch (typeChart){
            case TEMP:
                tvNameChart.setText("Temperature(" + "\u2103" + ")");
                line = new Line(yAxisValues).setColor(Color.parseColor("#FF2929"));
                break;
            case BRI:
                tvNameChart.setText("Brightness(%)");
                line = new Line(yAxisValues).setColor(Color.parseColor("#FFF429"));
                break;
            case HUM:
                tvNameChart.setText("Humidity(%)");
                line = new Line(yAxisValues).setColor(Color.parseColor("#29CFFF"));
                break;
        }

        model = ViewModelProviders.of(getActivity()).get(DeviceLogViewModel.class);
        model.getAllData(deviceID).observe(getActivity(), new Observer<List<DeviceEntity>>() {
            @Override
            public void onChanged(@Nullable List<DeviceEntity> deviceEntities) {
                yAxisValues.clear();
                switch (typeChart){
                    case TEMP:
                        for (int i = 0; i < deviceEntities.size(); i++) {
                            yAxisValues.add(new PointValue(i, deviceEntities.get(i).getTemp()));
                        }
                        break;
                    case BRI:
                        for (int i = 0; i < deviceEntities.size(); i++) {
                            yAxisValues.add(new PointValue(i, deviceEntities.get(i).getBright()));
                        }
                        break;
                    case HUM:
                        for (int i = 0; i < deviceEntities.size(); i++) {
                            yAxisValues.add(new PointValue(i, deviceEntities.get(i).getHumidity()));
                        }
                        break;
                }

                drawChart();
            }
        });

        drawEntireChart();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.deleteAll(deviceID);
            }
        });

        return view;
    }

    /**
     * Draw entire chart
     */
    private void drawEntireChart(){
        lines = new ArrayList();
        data = new LineChartData();

        Axis axis = new Axis();
        axis.setName("Numbers of message");
        axis.setValues(null);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#FFFFFF"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#FFFFFF"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        drawChart();
    }

    /**
     * Draw chart
     */
    private void drawChart(){
        lines.add(line);
        data.setLines(lines);

        lineChartView.setLineChartData(data);
        viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 100;
        viewport.bottom = 0;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
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

    /**
     * Subscribe connect event from main activity
     * @param connectStatusEvent
     */
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void onEvent(ConnectStatusEvent connectStatusEvent){
        if(connectStatusEvent.isConnected() == false)
            getActivity().onBackPressed();
    }
}
