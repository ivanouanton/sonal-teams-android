package com.waveneuro.ui.adapter.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waveneuro.R;
import com.waveneuro.data.model.entity.BleDevice;

public class DeviceDelegate {

    public DeviceViewHolder onCreateNoteViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    public void onBindNoteViewHolder(DeviceViewHolder holder, int position, BleDevice data, OnDeviceItemClickListener listener) {
        try {
            holder.bind(data);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onClickDevice(data);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
