package com.waveneuro.domain.model.common

import com.squareup.moshi.Json

enum class TosStatus {
    @Json(name = "0") SIGNED,
    @Json(name = "1") NOT_SIGNED,
    @Json(name = "2") WAITING_SIGNATURE,
    @Json(name = "3") SIGNED_MANUALLY
}