package com.waveneuro.data.api.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import com.waveneuro.domain.model.common.TosStatus

class TosStatusAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader): TosStatus {
        return when (jsonReader.nextString()) {
            "0" -> TosStatus.SIGNED
            "1" -> TosStatus.NOT_SIGNED
            "2" -> TosStatus.WAITING_SIGNATURE
            "3" -> TosStatus.SIGNED_MANUALLY
            else -> throw IllegalArgumentException("Unknown enum value")
        }
    }

    @ToJson
    fun toJson(jsonWriter: JsonWriter, value: TosStatus?) {
        if (value == null) {
            jsonWriter.nullValue()
            return
        }
        jsonWriter.value(value.name)
    }
}
