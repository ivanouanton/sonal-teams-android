package com.waveneuro.data.api.user.model.device

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSonalDevicesRs(
    @Json(name = "devices") val devices: List<ApiDevice>
)