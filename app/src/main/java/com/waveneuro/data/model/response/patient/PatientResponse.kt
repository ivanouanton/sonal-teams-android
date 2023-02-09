package com.waveneuro.data.model.response.patient

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName
import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.data.model.response.common.TosStatus
import java.util.*

data class PatientResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("birthday") val birthday: Date?,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String?,
    @SerializedName("sex") val isMale: Boolean,
    @SerializedName("organization") val organization: OrganizationResponse,
    @SerializedName("tos_signed") val isTosSigned: Boolean,
    @SerializedName("tos_status") val tosStatus: TosStatus?,
    @SerializedName("alternative_ids") val alternativeIds: List<AlternativeId>?,
    @SerializedName("image_thumbnail_url") val imageURLString: String?,
) : BaseModel()
