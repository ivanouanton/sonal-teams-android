package com.waveneuro.data.api.user

import com.waveneuro.data.api.user.model.device.ApiSonalDevicesRs
import com.waveneuro.data.api.user.model.login.ApiLoginMfaRs
import com.waveneuro.data.api.user.model.login.ApiLoginRq
import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRq
import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRs
import com.waveneuro.data.api.user.model.password.ApiForgotPasswordRq
import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRq
import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRs
import com.waveneuro.data.api.user.model.user.ApiUserInfoRs
import com.waveneuro.data.api.user.model.user.ApiUserUpdateRq
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {

    @POST("login")
    suspend fun login(@Body request: ApiLoginRq): ApiLoginMfaRs

    @POST("users/forgot_password")
    suspend fun forgotPassword(@Body request: ApiForgotPasswordRq)

    @GET("users/me")
    suspend fun getUserInfo(): ApiUserInfoRs

    @PUT("users/me")
    suspend fun updateUser(@Body request: ApiUserUpdateRq): ApiUserInfoRs

//    @POST("forgot_password_confirm")
//    fun forgotPasswordConfirm(@Body request: ForgotPasswordConfirmRequest?): Observable<ForgotPasswordConfirmResponse?>?
//
//    @POST("refresh-token")
//    fun refreshToken(): Observable<RefreshResponse?>?

    @POST("users/confirm_forgot_password")
    suspend fun changeTempPassword(@Body request: ApiSetNewPasswordRq): ApiSetNewPasswordRs

//    @POST("forgot_password_confirm")
//    fun changePassword(@Body request: SetPasswordRequest?): Observable<SetPasswordResponse?>?

    @GET("sonal/user_devices")
    suspend fun getSonalDevices(): ApiSonalDevicesRs

    @POST("confirm-software-token")
    suspend fun confirmSoftwareToken(@Body request: ApiConfirmTokenRq): ApiConfirmTokenRs

//    @GET("patients")
//    fun getClientList(
//        @Query("page") page: Int,
//        @Query("organization") org: Array<Int?>?,
//        @Query("search") startsWith: String?
//    ): Observable<ClientListResponse?>?
//
//    @GET("patients/{id}")
//    fun getClient(@Path("id") id: Int): Observable<ClientResponse?>?
//
//    @PUT("patients/{id}")
//    fun updateClient(
//        @Path("id") id: Int,
//        @Body request: ClientRequest?
//    ): Observable<ClientResponse?>?
//
//    @Headers("X-Client: Android")
//    @GET("sonal/sessions/{id}")
//    fun getSessions(@Path("id") id: Int): Observable<SessionResponse?>?
//
//    @GET("sonal/protocols/{id}")
//    fun getProtocolForUser(@Path("id") id: Int): Observable<ProtocolResponse?>?
//
//    @GET("orgs/me")
//    fun getOrganizations(): Observable<List<OrganizationResponse?>?>?

}