package com.waveneuro.data.model.response.password.password;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class SetNewPasswordResponse extends BaseModel {

    @SerializedName("msg")
    private String message;
    @SerializedName("error")
    private String error;
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("refresh_token")
    String refreshToken;


    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
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
}
