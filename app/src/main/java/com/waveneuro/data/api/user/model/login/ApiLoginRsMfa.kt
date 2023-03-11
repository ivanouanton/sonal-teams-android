package com.waveneuro.data.api.user.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class ApiLoginRsMfa(
    @Json(name = "ChallengeName") val challengeName: String?,
    @Json(name = "Session") val session: String,
)
