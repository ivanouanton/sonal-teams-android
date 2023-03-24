package com.waveneuro.data.api.user.model.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDevice(
    @Json(name = "last_session_at") val lastSessionAt: Long,
    @Json(name = "sonal_id") val deviceName: String,
)