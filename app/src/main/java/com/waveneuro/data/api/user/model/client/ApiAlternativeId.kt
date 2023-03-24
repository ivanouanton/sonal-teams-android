package com.waveneuro.data.api.user.model.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiAlternativeId(
    @Json(name = "alt_id") val id: String,
    @Json(name = "name") val name: String
)
