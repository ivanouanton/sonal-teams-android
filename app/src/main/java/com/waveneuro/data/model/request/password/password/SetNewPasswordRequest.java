package com.waveneuro.data.model.request.password.password;

import com.google.gson.annotations.SerializedName;

public class SetNewPasswordRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("code")
    private String code;

    @SerializedName("password")
    private String password;

    public SetNewPasswordRequest(String email, String code, String password) {
        this.email = email;
        this.code = code;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
