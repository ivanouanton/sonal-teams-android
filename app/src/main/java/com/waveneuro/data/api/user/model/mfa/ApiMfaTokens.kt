package com.waveneuro.data.api.user.model.mfa

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiMfaTokens(
    @Json(name = "AccessToken") val accessToken: String,
    @Json(name = "IdToken") val idToken: String,
    @Json(name = "RefreshToken") val refreshToken: String,
)
