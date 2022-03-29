package com.waveneuro.ui.device;

import android.os.Bundle;

import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDeviceActivity extends BaseActivity {


    @BindView(R.id.tv_device_name)
    MaterialTextView tvDeviceName;

    String deviceName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(MyDeviceCommand.DEVICE_NAME)) {
            deviceName = getIntent().getStringExtra(MyDeviceCommand.DEVICE_NAME);
        }
        setView();
        setObserver();
    }

    private void setView() {
        tvDeviceName.setText(deviceName);
    }

    private void setObserver() {

    }

    @OnClick(R.id.iv_back)
    public void OnClickBack() {
        onBackPressed();
    }
}