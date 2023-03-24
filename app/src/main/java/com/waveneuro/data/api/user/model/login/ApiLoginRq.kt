package com.waveneuro.data.api.user.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiLoginRq(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
)