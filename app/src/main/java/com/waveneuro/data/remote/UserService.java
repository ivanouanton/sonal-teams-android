package com.waveneuro.data.remote;

import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.response.login.LoginResponse;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET("me")
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

    @PUT("me")
    Observable<UserInfoResponse> updateUser(@Body AccountUpdateRequest request);
}
