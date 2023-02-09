package com.waveneuro.data.model.response.common

import com.google.gson.annotations.SerializedName

enum class TosStatus() {
    @SerializedName("0") SIGNED,
    @SerializedName("1") NOT_SIGNED,
    @SerializedName("2") WAITING_SIGNATURE,
    @SerializedName("3") SIGNED_MANUALLY
}