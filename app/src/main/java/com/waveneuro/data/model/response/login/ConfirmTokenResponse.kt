//package com.waveneuro.data.model.response.login
//
//import com.asif.abase.data.model.BaseModel
//import com.google.gson.annotations.SerializedName
//import com.google.gson.annotations.Expose
//
//class ConfirmTokenResponse(@field:Expose @field:SerializedName("AuthenticationResult")
//                           var authenticationResult: AuthenticationResult) :
//    BaseModel() {
//    var error: String? = null
//
//    val idToken: String
//        get() = authenticationResult.idToken
//
//    override fun toString(): String {
//        return "ConfirmTokenResponse{" +
//                "error='" + error + '\'' +
//                ", authenticationResult=" + authenticationResult +
//                '}'
//    }
//}
//
//class AuthenticationResult(
//    @field:SerializedName("AccessToken") var accessToken: String,
//    @field:SerializedName(
//        "ExpiresIn"
//    ) var expiresIn: Int,
//    @field:SerializedName("IdToken") var idToken: String,
//    @field:SerializedName(
//        "RefreshToken"
//    ) var refreshToken: String
//) {
//
//    override fun toString(): String {
//        return "AuthenticationResult{" +
//                "accessToken='" + accessToken + '\'' +
//                ", expiresIn=" + expiresIn +
//                ", idToken='" + idToken + '\'' +
//                ", refreshToken='" + refreshToken + '\'' +
//                '}'
//    }
//}
//
////public class ConfirmTokenResponse extends BaseModel {
////
////    String error;
////
////    @SerializedName("AuthenticationResult")
////    @Expose
////    private AuthenticationResult authenticationResult;
////
////    public ConfirmTokenResponse(AuthenticationResult authenticationResult) {
////        this.authenticationResult = authenticationResult;
////    }
////
////    public String getError() {
////        return error;
////    }
////
////    public void setError(String error) {
////        this.error = error;
////    }
////
////    public AuthenticationResult getAuthenticationResult() {
////        return authenticationResult;
////    }
////
////    public void setAuthenticationResult(AuthenticationResult authenticationResult) {
////        this.authenticationResult = authenticationResult;
////    }
////
////    public String getIdToken() {
////        return authenticationResult.getIdToken();
////    }
////
////    @Override
////    public String toString() {
////        return "ConfirmTokenResponse{" +
////                "error='" + error + '\'' +
////                ", authenticationResult=" + authenticationResult +
////                '}';
////    }
////}
//
////class AuthenticationResult {
////
////    @SerializedName("AccessToken")
////    private String accessToken;
////
////    @SerializedName("ExpiresIn")
////    private int expiresIn;
////
////    @SerializedName("IdToken")
////    private String idToken;
////
////    @SerializedName("RefreshToken")
////    private String refreshToken;
////
////    public AuthenticationResult(String accessToken, int expiresIn, String idToken, String refreshToken) {
////        this.accessToken = accessToken;
////        this.expiresIn = expiresIn;
////        this.idToken = idToken;
////        this.refreshToken = refreshToken;
////    }
////
////    public String getAccessToken() {
////        return accessToken;
////    }
////
////    public void setAccessToken(String accessToken) {
////        this.accessToken = accessToken;
////    }
////
////    public int getExpiresIn() {
////        return expiresIn;
////    }
////
////    public void setExpiresIn(int expiresIn) {
////        this.expiresIn = expiresIn;
////    }
////
////    public String getIdToken() {
////        return idToken;
////    }
////
////    public void setIdToken(String idToken) {
////        this.idToken = idToken;
////    }
////
////    public String getRefreshToken() {
////        return refreshToken;
////    }
////
////    public void setRefreshToken(String refreshToken) {
////        this.refreshToken = refreshToken;
////    }
////
////    @Override
////    public String toString() {
////        return "AuthenticationResult{" +
////                "accessToken='" + accessToken + '\'' +
////                ", expiresIn=" + expiresIn +
////                ", idToken='" + idToken + '\'' +
////                ", refreshToken='" + refreshToken + '\'' +
////                '}';
////    }
////}