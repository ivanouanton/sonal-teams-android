package com.waveneuro.data.api.user.model.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.waveneuro.data.api.user.model.user.ApiOrganizationRs
import com.waveneuro.domain.model.common.TosStatus

@JsonClass(generateAdapter = true)
data class ApiClientRs(
    @Json(name = "id") val id: Int,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "birthday") val birthday: String?,
    @Json(name = "email") val email: String,
    @Json(name = "username") val username: String?,
    @Json(name = "sex") val isMale: Boolean,
    @Json(name = "organization") val organization: ApiOrganizationRs,
    @Json(name = "tos_signed") val isTosSigned: Boolean,
    @Json(name = "tos_status") val tosStatus: TosStatus,
    @Json(name = "alternative_ids") val alternativeIds: List<ApiAlternativeId>?,
    @Json(name = "image_thumbnail_url") val imageURLString: String?,
)
