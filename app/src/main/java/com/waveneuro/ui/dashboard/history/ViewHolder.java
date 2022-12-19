package com.waveneuro.ui.dashboard.history;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public final View view;
    public final TextView deviceName, deviceLastConnection;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;

        deviceName = this.view.findViewById(R.id.tv_device_name);
        deviceLastConnection = this.view.findViewById(R.id.tv_device_last_connection);
    }
}
