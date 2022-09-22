package com.waveneuro.data.model.response.login;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class LoginResponseMfa extends BaseModel {

    String error;

    @SerializedName("ChallengeName")
    String challengeName;

    @SerializedName("Session")
    String session;

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
