package com.waveneuro.data.api.user.model.login;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class ApiLoginResponse extends BaseModel {

    String error;

    @SerializedName("access_token")
    String accessToken;
    @SerializedName("refresh_token")
    String refreshToken;
    @SerializedName("is_first_entrance")
    boolean isFirstEntrance;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isFirstEntrance() {
        return isFirstEntrance;
    }

    public void setFirstEntrance(boolean firstEntrance) {
        isFirstEntrance = firstEntrance;
    }
}
