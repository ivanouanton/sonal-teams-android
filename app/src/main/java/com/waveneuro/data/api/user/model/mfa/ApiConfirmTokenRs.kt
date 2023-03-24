package com.waveneuro.data.api.user.model.mfa

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiConfirmTokenRs(
    @Json(name = "AuthenticationResult") val tokens: ApiMfaTokens,
)
