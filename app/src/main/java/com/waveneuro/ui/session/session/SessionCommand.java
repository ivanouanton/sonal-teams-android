package com.waveneuro.ui.session.session;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.data.model.BaseModel;
import com.asif.abase.injection.qualifier.ActivityContext;
import com.waveneuro.data.model.entity.BleDevice;

import javax.inject.Inject;

public class SessionCommand extends NavigationCommand {

    public static final String BLE_DEVICE = "ble_device";
    public static final String TREATMENT_LENGTH = "treatment_length";
    public static final String PROTOCOL_FREQUENCY = "protocol_frequency";

    private final Context mContext;

    @Inject
    public SessionCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, SessionActivity.class);
        this.mContext.startActivity(intent);
    }

    @Override
    public void navigate(BaseModel model) {
        if (model instanceof BleDevice) {
            Intent intent = new Intent(this.mContext, SessionActivity.class);
            intent.putExtra(BLE_DEVICE, ((BleDevice) model));
            this.mContext.startActivity(intent);
        }

    }

    public void navigate(String treatmentLength, String protocolFrequency) {
        Intent intent = new Intent(this.mContext, SessionActivity.class);
        intent.putExtra(TREATMENT_LENGTH, treatmentLength);
        intent.putExtra(PROTOCOL_FREQUENCY, protocolFrequency);
        this.mContext.startActivity(intent);
    }
}
