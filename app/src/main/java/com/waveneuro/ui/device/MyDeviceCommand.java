package com.waveneuro.ui.device;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class MyDeviceCommand extends NavigationCommand {

    public static final String DEVICE_NAME = "device_name";

    private final Context mContext;

    @Inject
    public MyDeviceCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate(String value) {
        Intent intent = new Intent(this.mContext, MyDeviceActivity.class);
        intent.putExtra(DEVICE_NAME, value);
        this.mContext.startActivity(intent);
    }
}