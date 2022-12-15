package com.waveneuro.ui.dashboard.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.response.device.SonalDevicesResponse;

import java.util.ArrayList;

public class DevicesHistoryAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;
    private ArrayList<SonalDevicesResponse.Device> devices;

    public DevicesHistoryAdapter(Context context, ArrayList<SonalDevicesResponse.Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_history_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SonalDevicesResponse.Device device = devices.get(position);

        holder.deviceName.setText(device.getDeviceName());
        holder.deviceLastConnection.setText(device.getLastConnection());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}
