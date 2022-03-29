package com.waveneuro.data.model.entity;

import com.asif.abase.data.model.BaseModel;

import java.io.Serializable;

public class BleDevice extends BaseModel implements Serializable {
    String name;
    String mac;
    String key;

    public BleDevice(com.ap.ble.data.BleDevice bleDevice) {
        this.name = bleDevice.getName();
        this.mac = bleDevice.getMac();
        this.key = bleDevice.getKey();
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }

    public String getKey() {
        return key;
    }
}
