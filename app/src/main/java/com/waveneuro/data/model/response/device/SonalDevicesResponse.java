package com.waveneuro.data.model.response.device;

import android.annotation.SuppressLint;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SonalDevicesResponse extends BaseModel {
    @SerializedName("devices")
    @Expose
    private List<Device> devices;

    public ArrayList<SonalDevicesResponse.Device> getDevices() {
        return new ArrayList<Device>(devices);
    }

    public class Device {
        @SerializedName("last_session_at")
        private int last_session_at;

        @SerializedName("sonal_id")
        private String deviceName;

        public String getDeviceName() {
            return deviceName;
        }

        @SuppressLint("SimpleDateFormat")
        public String getLastConnection() {
            Date date = new java.util.Date(last_session_at*1000L);
            return "Last connection: " + new SimpleDateFormat("MMM d, yyyy").format(date);
        }
    }
}
