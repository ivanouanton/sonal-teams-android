package com.waveneuro.data.api.user.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiOrganizationRs(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)