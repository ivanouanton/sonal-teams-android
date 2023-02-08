package com.waveneuro.data.model.response.user

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class RefreshResponse(

    @SerializedName("msg")
    var message: String? = null,

    @SerializedName("error")
    var error: String? = null,

    @SerializedName("access_token")
    var accessToken: String? = null

) : BaseModel()