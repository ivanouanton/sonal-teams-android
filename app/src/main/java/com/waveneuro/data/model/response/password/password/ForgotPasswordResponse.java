package com.waveneuro.data.model.response.password.password;

import com.asif.abase.data.model.BaseModel;

public class ForgotPasswordResponse extends BaseModel {

    private String error;

    private String msg;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
