package com.waveneuro.data.api.user.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiUserUpdateRq(
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String
)