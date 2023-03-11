package com.waveneuro.data.remote;

import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.request.client.ClientRequest;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.response.client.ClientListResponse;
import com.waveneuro.data.model.response.client.ClientResponse;
import com.waveneuro.data.model.response.device.SonalDevicesResponse;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;
import com.waveneuro.data.model.response.organization.OrganizationResponse;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

//    @POST("login")
//    Observable<LoginResponseMfa> login(@Body LoginRequest request);

    @GET("users/me")
    Observable<UserInfoResponse> getPersonalInfo();

    @POST("users/forgot_password")
    Observable<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("forgot_password_confirm")
    Observable<ForgotPasswordConfirmResponse> forgotPasswordConfirm(@Body ForgotPasswordConfirmRequest request);

    @POST("refresh-token")
    Observable<RefreshResponse> refreshToken();

    @POST("users/confirm_forgot_password")
    Observable<SetNewPasswordResponse> changeTempPassword(@Body SetNewPasswordRequest request);

    @POST("forgot_password_confirm")
    Observable<SetPasswordResponse> changePassword(@Body SetPasswordRequest request);

    @PUT("users/me")
    Observable<UserInfoResponse> updateUser(@Body AccountUpdateRequest request);

    @GET("sonal/user_devices")
    Observable<SonalDevicesResponse> getSonalDevices();

    @POST("confirm-software-token")
    Observable<ConfirmTokenResponse> confirmSoftwareToken(@Body ConfirmTokenRequest request);

    @GET("patients")
    Observable<ClientListResponse> getClientList(@Query("page") int page, @Query("organization") Integer[] org, @Query("search") String startsWith);

    @GET("patients/{id}")
    Observable<ClientResponse> getClient(@Path("id") int id);

    @PUT("patients/{id}")
    Observable<ClientResponse> updateClient(@Path("id") int id, @Body ClientRequest request);

    @Headers("X-Client: Android")
    @GET("sonal/sessions/{id}")
    Observable<SessionResponse> getSessions(@Path("id") int id);

    @GET("sonal/protocols/{id}")
    Observable<ProtocolResponse> getProtocolForUser(@Path("id") int id);

    @GET("orgs/me")
    Observable<List<OrganizationResponse>> getOrganizations();
}
