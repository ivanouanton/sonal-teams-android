package com.waveneuro.data.api.user.model.mfa

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiConfirmTokenRq(
    @Json(name = "username") val username: String,
    @Json(name = "mfa_code") val mfaCode: String,
    @Json(name = "session") val session: String
)
