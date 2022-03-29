package com.waveneuro.data.model.response.user;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class RefreshResponse extends BaseModel {

    @SerializedName("msg")
    String message;
    @SerializedName("error")
    String error;
    @SerializedName("access_token")
    String accessToken;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
}
