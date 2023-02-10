package com.waveneuro.data.model.response.client

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class AlternativeId(
    @SerializedName("alt_id") val id: String,
    @SerializedName("name") val name: String
) : BaseModel()
