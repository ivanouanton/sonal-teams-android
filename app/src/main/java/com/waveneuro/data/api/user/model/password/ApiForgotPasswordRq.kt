package com.waveneuro.data.api.user.model.password

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiForgotPasswordRq(
    @Json(name = "email") val email: String
)