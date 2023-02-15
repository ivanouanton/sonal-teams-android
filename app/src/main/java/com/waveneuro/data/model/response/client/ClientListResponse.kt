package com.waveneuro.data.model.response.client

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class ClientListResponse(
    @SerializedName("has_next") val hasNext: Boolean,
    @SerializedName("has_prev") val hasPrev: Boolean,
    @SerializedName("next_num") val nextNum: Int?,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("patients") val patients: List<ClientResponse>,
    @SerializedName("total") val total: Int,
) : BaseModel()