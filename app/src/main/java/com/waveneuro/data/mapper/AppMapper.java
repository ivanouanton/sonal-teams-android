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
        user.setUsername(response.getUsername());
        user.setGivenName(response.getLastName());
        user.setFamilyName(response.getFamilyName());
        user.setGender(response.getGender());
        user.setBirthdate(response.getBirthdate());
        user.setImageThumbnailUrl(response.getImageThumbnailUrl());
        user.setCustomGoal(response.getCustomGoal());
        user.setLocation(response.getLocation());
        return user;
    }

    public static BleDevice toAppBleDevice(com.ap.ble.data.BleDevice response) {
        return new BleDevice(response);
    }
}
