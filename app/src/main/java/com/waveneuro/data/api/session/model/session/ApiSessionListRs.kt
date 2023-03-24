package com.waveneuro.data.api.session.model.session

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSessionListRs(
    @Json(name = "sessions") val sessions: List<ApiSession>
)