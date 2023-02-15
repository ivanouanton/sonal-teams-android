package com.waveneuro.data.model.response.user

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("AuthenticationResult") val authResult: Token,
) : BaseModel()