package com.waveneuro.data.model.request.account.update;

import com.google.gson.annotations.SerializedName;

public class AccountUpdateRequest {

    @SerializedName("family_name")
    String familyName;

    @SerializedName("given_name")
    String givenName;

    @SerializedName("username")
    String username;

    @SerializedName("email")
    String email;

    @SerializedName("birthdate")
    String birthDate;

    public AccountUpdateRequest() {
    }

    public AccountUpdateRequest(String familyName, String givenName,
                                String username, String email, String birthDate) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
