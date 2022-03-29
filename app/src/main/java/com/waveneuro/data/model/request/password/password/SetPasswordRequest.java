package com.waveneuro.data.model.request.password.password;

import com.google.gson.annotations.SerializedName;

public class SetPasswordRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("code")
    private String code;

    public SetPasswordRequest(String username, String password, String code) {
        this.username = username;
        this.password = password;
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SetPasswordRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
