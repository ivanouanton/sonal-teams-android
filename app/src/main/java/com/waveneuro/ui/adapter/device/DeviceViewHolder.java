package com.waveneuro.ui.adapter.device;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.entity.BleDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;

    public DeviceViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(BleDevice data) {
        tvDeviceName.setText("" + data.getName());
    }
}
