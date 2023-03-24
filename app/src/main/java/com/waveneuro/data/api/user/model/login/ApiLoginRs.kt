package com.waveneuro.data.api.user.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiLoginRs(
    @Json(name = "access_token") val accessToken: String?,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "is_first_entrance") val isFirstEntrance: Boolean
)