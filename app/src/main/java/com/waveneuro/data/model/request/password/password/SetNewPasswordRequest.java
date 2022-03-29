package com.waveneuro.data.model.request.password.password;

import com.google.gson.annotations.SerializedName;

public class SetNewPasswordRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("new_password")
    private String newPassword;

    @SerializedName("old_password")
    private String oldPassword;

    public SetNewPasswordRequest(String username, String newPassword, String oldPassword) {
        this.username = username;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "SetNewPasswordRequest{" +
                "username='" + username + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                '}';
    }
}
