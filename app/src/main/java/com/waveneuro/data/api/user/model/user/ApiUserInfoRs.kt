package com.waveneuro.data.api.user.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiUserInfoRs(
    @Json(name = "id") val id: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "email") val email: String,
    @Json(name = "role") val role: String,
    @Json(name = "organizations") val organizations: List<ApiOrganizationRs>
)
