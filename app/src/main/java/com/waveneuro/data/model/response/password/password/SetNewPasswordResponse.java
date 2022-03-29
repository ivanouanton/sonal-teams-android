package com.waveneuro.data.model.response.password.password;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class SetNewPasswordResponse extends BaseModel {
    @SerializedName("msg")
    private String message;

    @SerializedName("error")
    private String error;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
