package com.waveneuro.data.remote;

import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.request.device.SonalDeviceRequest;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.request.patient.PatientRequest;
import com.waveneuro.data.model.response.device.SonalDeviceResponse;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;
import com.waveneuro.data.model.response.login.LoginResponseMfa;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.patient.PatientListResponse;
import com.waveneuro.data.model.response.patient.PatientResponse;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("login")
    Observable<LoginResponseMfa> login(@Body LoginRequest request);

    @GET("users/me")
    Observable<UserInfoResponse> getPersonalInfo();

    @POST("forgot_password")
    Observable<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("forgot_password_confirm")
    Observable<ForgotPasswordConfirmResponse> forgotPasswordConfirm(@Body ForgotPasswordConfirmRequest request);

    @POST("refresh")
    Observable<RefreshResponse> refreshToken();

    @POST("change_temp_password")
    Observable<SetNewPasswordResponse> changeTempPassword(@Body SetNewPasswordRequest request);

    @POST("forgot_password_confirm")
    Observable<SetPasswordResponse> changePassword(@Body SetPasswordRequest request);

    @PUT("users/me")
    Observable<UserInfoResponse> updateUser(@Body AccountUpdateRequest request);

    @GET("sonal-devices")
    Observable<List<SonalDeviceResponse>> getSonalDevices();

    @POST("sonal-devices")
    Observable<SonalDeviceResponse> addSonalDevice(@Body SonalDeviceRequest request);

    @POST("confirm-software-token")
    Observable<ConfirmTokenResponse> confirmSoftwareToken(@Body ConfirmTokenRequest request);

    @GET("patients")
    Observable<PatientListResponse> getClientList();

    @GET("patients/{id}")
    Observable<PatientResponse> getClient(@Path("id") int id);

    @PUT("patients/{id}")
    Observable<PatientResponse> updateClient(@Path("id") int id, @Body PatientRequest request);

    @GET("sonal/sessions/{id}")
    Observable<SessionResponse> getSessions(@Path("id") int id);

}
