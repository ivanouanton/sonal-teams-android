package com.waveneuro.data.api.user

import com.waveneuro.data.api.session.model.session.ApiSessionListRs
import com.waveneuro.data.api.user.model.client.ApiClientListRs
import com.waveneuro.data.api.user.model.client.ApiClientRq
import com.waveneuro.data.api.user.model.client.ApiClientRs
import com.waveneuro.data.api.user.model.device.ApiSonalDevicesRs
import com.waveneuro.data.api.user.model.login.ApiLoginMfaRs
import com.waveneuro.data.api.user.model.login.ApiLoginRq
import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRq
import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRs
import com.waveneuro.data.api.user.model.password.ApiForgotPasswordRq
import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRq
import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRs
import com.waveneuro.data.api.user.model.protocol.ApiProtocolRs
import com.waveneuro.data.api.user.model.user.ApiOrganizationRs
import com.waveneuro.data.api.user.model.user.ApiUserInfoRs
import com.waveneuro.data.api.user.model.user.ApiUserUpdateRq
import retrofit2.http.*

interface UserApi {

    @POST("login")
    suspend fun login(@Body request: ApiLoginRq): ApiLoginMfaRs

    @POST("confirm-software-token")
    suspend fun confirmSoftwareToken(@Body request: ApiConfirmTokenRq): ApiConfirmTokenRs

    @POST("users/forgot_password")
    suspend fun forgotPassword(@Body request: ApiForgotPasswordRq)

    @GET("users/me")
    suspend fun getUserInfo(): ApiUserInfoRs

    @PUT("users/me")
    suspend fun updateUser(@Body request: ApiUserUpdateRq): ApiUserInfoRs

    @POST("users/confirm_forgot_password")
    suspend fun changeTempPassword(@Body request: ApiSetNewPasswordRq): ApiSetNewPasswordRs

    @GET("sonal/user_devices")
    suspend fun getSonalDevices(): ApiSonalDevicesRs

    @GET("patients")
    suspend fun getClientList(
        @Query("page") page: Int,
        @Query("search") startsWith: String,
        @Query("organization") org: List<Int>
    ): ApiClientListRs

    @GET("patients/{id}")
    suspend fun getClient(@Path("id") id: Int): ApiClientRs

    @PUT("patients/{id}")
    suspend fun updateClient(
        @Path("id") id: Int,
        @Body request: ApiClientRq
    ): ApiClientRs

    @GET("sonal/sessions/{id}")
    suspend fun getSessions(@Path("id") id: Int): ApiSessionListRs

    @GET("sonal/protocols/{id}")
    suspend fun getProtocolForUser(@Path("id") id: Int): ApiProtocolRs

    @GET("orgs/me")
    suspend fun getOrganizations(): List<ApiOrganizationRs>

}