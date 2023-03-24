package com.waveneuro.data.api.user.model.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waveneuro.domain.model.common.SexType

@JsonClass(generateAdapter = true)
data class ApiClientRq(
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "birthday") val birthday: String?,
    @Json(name = "email") val email: String,
    @Json(name = "sex") val sex: SexType,
)
