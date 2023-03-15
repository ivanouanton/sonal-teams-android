package com.waveneuro.data.api.user.model.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiClientListRs(
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "has_prev") val hasPrev: Boolean,
    @Json(name = "next_num") val nextNum: Int?,
    @Json(name = "page") val page: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "patients") val patients: List<ApiClientRs>,
    @Json(name = "total") val total: Int,
)