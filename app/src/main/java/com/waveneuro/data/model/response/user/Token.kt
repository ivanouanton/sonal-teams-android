package com.waveneuro.data.model.response.user

import com.asif.abase.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("IdToken") val accessToken: String,
    @SerializedName("RefreshToken") val refreshToken: String
) : BaseModel()
