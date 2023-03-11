package com.waveneuro.data.api.user

import com.waveneuro.data.api.user.model.login.ApiLoginRq
import com.waveneuro.data.api.user.model.login.ApiLoginRsMfa
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest
import com.waveneuro.data.model.request.client.ClientRequest
import com.waveneuro.data.model.request.login.ConfirmTokenRequest
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest
import com.waveneuro.data.model.request.password.password.SetPasswordRequest
import com.waveneuro.data.model.response.client.ClientListResponse
import com.waveneuro.data.model.response.client.ClientResponse
import com.waveneuro.data.model.response.device.SonalDevicesResponse
import com.waveneuro.data.model.response.login.ConfirmTokenResponse
import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse
import com.waveneuro.data.model.response.password.password.SetPasswordResponse
import com.waveneuro.data.model.response.protocol.ProtocolResponse
import com.waveneuro.data.model.response.session.SessionResponse
import com.waveneuro.data.model.response.user.RefreshResponse
import com.waveneuro.data.model.response.user.UserInfoResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface UserApi {

    @POST("login")
    fun login(@Body request: ApiLoginRq): ApiLoginRsMfa

    @GET("users/me")
    fun getPersonalInfo(): Observable<UserInfoResponse?>?

    @POST("users/forgot_password")
    fun forgotPassword(@Body request: ForgotPasswordRequest?): Observable<ForgotPasswordResponse?>?

    @POST("forgot_password_confirm")
    fun forgotPasswordConfirm(@Body request: ForgotPasswordConfirmRequest?): Observable<ForgotPasswordConfirmResponse?>?

    @POST("refresh-token")
    fun refreshToken(): Observable<RefreshResponse?>?

    @POST("users/confirm_forgot_password")
    fun changeTempPassword(@Body request: SetNewPasswordRequest?): Observable<SetNewPasswordResponse?>?

    @POST("forgot_password_confirm")
    fun changePassword(@Body request: SetPasswordRequest?): Observable<SetPasswordResponse?>?

    @PUT("users/me")
    fun updateUser(@Body request: AccountUpdateRequest?): Observable<UserInfoResponse?>?

    @GET("sonal/user_devices")
    fun getSonalDevices(): Observable<SonalDevicesResponse?>?

    @POST("confirm-software-token")
    fun confirmSoftwareToken(@Body request: ConfirmTokenRequest?): Observable<ConfirmTokenResponse?>?

    @GET("patients")
    fun getClientList(
        @Query("page") page: Int,
        @Query("organization") org: Array<Int?>?,
        @Query("search") startsWith: String?
    ): Observable<ClientListResponse?>?

    @GET("patients/{id}")
    fun getClient(@Path("id") id: Int): Observable<ClientResponse?>?

    @PUT("patients/{id}")
    fun updateClient(
        @Path("id") id: Int,
        @Body request: ClientRequest?
    ): Observable<ClientResponse?>?

    @Headers("X-Client: Android")
    @GET("sonal/sessions/{id}")
    fun getSessions(@Path("id") id: Int): Observable<SessionResponse?>?

    @GET("sonal/protocols/{id}")
    fun getProtocolForUser(@Path("id") id: Int): Observable<ProtocolResponse?>?

    @GET("orgs/me")
    fun getOrganizations(): Observable<List<OrganizationResponse?>?>?

}