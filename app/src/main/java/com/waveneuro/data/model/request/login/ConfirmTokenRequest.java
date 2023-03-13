//package com.waveneuro.data.model.request.login;
//
//import com.google.gson.annotations.SerializedName;
//
//public class ConfirmTokenRequest {
//    @SerializedName("username")
//    private String username;
//
//    @SerializedName("mfa_code")
//    private String mfaCode;
//
//    @SerializedName("session")
//    private String session;
//
//    public ConfirmTokenRequest(String username, String mfaCode, String session) {
//        this.username = username;
//        this.mfaCode = mfaCode;
//        this.session = session;
//    }
//
//    @Override
//    public String toString() {
//        return "ConfirmTokenRequest{" +
//                "username='" + username + '\'' +
//                ", password='" + mfaCode + '\'' +
//                ", session='" + session + '\'' +
//                '}';
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return mfaCode;
//    }
//
//    public void setPassword(String password) {
//        this.mfaCode = password;
//    }
//
//    public String getSession() {
//        return session;
//    }
//
//    public void setSession(String session) {
//        this.session = session;
//    }
//}
