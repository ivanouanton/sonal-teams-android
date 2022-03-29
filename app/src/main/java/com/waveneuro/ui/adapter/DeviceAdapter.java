package com.waveneuro.ui.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.asif.abase.utils.RecyclerViewAdapter;
import com.waveneuro.data.model.entity.BleDevice;
import com.waveneuro.ui.adapter.device.DeviceDelegate;
import com.waveneuro.ui.adapter.device.DeviceViewHolder;
import com.waveneuro.ui.adapter.device.OnDeviceItemClickListener;

import java.util.List;

public class DeviceAdapter extends RecyclerViewAdapter<BleDevice> {

    private final DeviceDelegate delegate;
    private final OnDeviceItemClickListener listener;

    private DeviceAdapter(List<BleDevice> list, DeviceDelegate delegate, OnDeviceItemClickListener listener) {
        this.dataList = list;
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewCustomHolder(ViewGroup parent, int viewType) {
        return this.delegate.onCreateNoteViewHolder(parent, viewType);
    }

    @Override
    protected void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        BleDevice data = this.dataList.get(position);
        this.delegate.onBindNoteViewHolder((DeviceViewHolder) holder, position, data, listener);
    }

    public static class Builder {
        private List<BleDevice> list;
        private DeviceDelegate delegate;
        private OnDeviceItemClickListener listener;

        public Builder setList(List<BleDevice> list) {
            this.list = list;
            return this;
        }

        public Builder setDelegate(DeviceDelegate delegate) {
            this.delegate = delegate;
            return this;
        }

        public Builder setOnNoteListener(OnDeviceItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public DeviceAdapter create() {
            if(this.delegate == null)
                this.delegate = new DeviceDelegate();
            return new DeviceAdapter(this.list, this.delegate, this.listener);
        }
    }
}