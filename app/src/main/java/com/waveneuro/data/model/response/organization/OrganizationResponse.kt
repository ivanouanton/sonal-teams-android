package com.waveneuro.data.model.response.organization

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class OrganizationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) : BaseModel()