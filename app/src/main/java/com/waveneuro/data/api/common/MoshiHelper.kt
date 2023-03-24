package com.waveneuro.data.api.common

import com.squareup.moshi.Moshi
import java.util.*

fun buildBaseMoshi(): Moshi = Moshi.Builder()
    .add(Date::class.java, Rfc1123DateJsonAdapter())
    .add(TosStatusAdapter())
    .build()
