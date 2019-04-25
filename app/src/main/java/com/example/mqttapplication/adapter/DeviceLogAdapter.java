package com.example.mqttapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mqttapplication.R;
import com.example.mqttapplication.roomdatabase.DeviceEntity;

import java.util.List;

public class DeviceLogAdapter extends RecyclerView.Adapter<DeviceLogAdapter.DeviceLogViewHoler> {
    private final LayoutInflater mInflater;
    private List<DeviceEntity> data;

    public void setData(List<DeviceEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public DeviceLogAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DeviceLogViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.item_device_log, viewGroup, false);
        return new DeviceLogViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceLogViewHoler deviceLogViewHoler, int position) {
        if((position % 2) == 0)
            deviceLogViewHoler.ln_item_log.setBackgroundResource(R.color.colorThird);
        else
            deviceLogViewHoler.ln_item_log.setBackgroundResource(R.color.colorSecondary);
        deviceLogViewHoler.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }


    static class DeviceLogViewHoler extends RecyclerView.ViewHolder{
        private TextView tvTime;
        private TextView tvMessageLog;
        private LinearLayout ln_item_log;

        public DeviceLogViewHoler(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
            tvMessageLog = (TextView)itemView.findViewById(R.id.tvMessageLog);
            ln_item_log = (LinearLayout)itemView.findViewById(R.id.ln_item_log);
        }

        void bind(DeviceEntity currentData){
            tvTime.setText(currentData.getTime());
            tvMessageLog.setText("Temp: " + String.format("%02d", currentData.getTemp()) + " | "
                    + "Humidity: " + String.format("%02d", currentData.getHumidity()) + " | "
                    + " Bright: " + String.format("%02d", currentData.getBright()));
        }
    }
}
