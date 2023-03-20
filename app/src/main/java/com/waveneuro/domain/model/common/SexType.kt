package com.waveneuro.domain.model.common

import com.squareup.moshi.Json

enum class SexType {
    @Json(name = "MALE") MALE,
    @Json(name = "FEMALE") FEMALE,
}