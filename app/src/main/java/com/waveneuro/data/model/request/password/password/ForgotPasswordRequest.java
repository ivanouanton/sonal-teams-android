package com.waveneuro.data.model.request.password.password;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {

    @SerializedName("username")
    private String username;

    public ForgotPasswordRequest(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ForgotPasswordRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
