package com.waveneuro.data.model.request.client

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName
import com.waveneuro.data.model.request.common.SexType

data class ClientRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("email") val email: String,
    @SerializedName("sex") val sex: SexType,
) : BaseModel()
