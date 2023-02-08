package com.waveneuro.data.mapper;

import com.waveneuro.data.model.entity.BleDevice;
import com.waveneuro.data.model.entity.User;
import com.waveneuro.data.model.response.user.UserInfoResponse;

public class AppMapper {

    private AppMapper(){}

    public static User toAppUser(UserInfoResponse response) {
        User user = new User(response.getId());
        user.setEmail(response.getEmail());
        user.setName(response.getFirstName());
        user.setGivenName(response.getLastName());
        return user;
    }

    public static BleDevice toAppBleDevice(com.ap.ble.data.BleDevice response) {
        return new BleDevice(response);
    }
}
