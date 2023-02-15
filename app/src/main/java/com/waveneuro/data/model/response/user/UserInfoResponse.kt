package com.waveneuro.data.model.response.user

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName
import com.waveneuro.data.model.response.organization.OrganizationResponse

data class UserInfoResponse(
    @SerializedName("id") val id: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("organizations") val organizations: List<OrganizationResponse>
) : BaseModel()
