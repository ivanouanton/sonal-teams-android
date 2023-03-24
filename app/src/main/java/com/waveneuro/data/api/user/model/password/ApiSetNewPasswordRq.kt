package com.waveneuro.data.api.user.model.password

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSetNewPasswordRq(
    @Json(name = "email") val email: String,
    @Json(name = "code") val code: String,
    @Json(name = "password") val password: String
)