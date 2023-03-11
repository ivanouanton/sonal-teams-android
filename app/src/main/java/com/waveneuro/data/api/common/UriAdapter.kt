package com.waveneuro.data.api.common

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Rfc1123DateJsonAdapter : JsonAdapter<Date?>() {

    private val dateFormatter = SimpleDateFormat(PATTERN_RFC1123, Locale.US)

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Date? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        val string = reader.nextString()

        return dateFormatter.parse(string)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value == null) {
            writer.nullValue()
        } else {
            val string = dateFormatter.format(value)
            writer.value(string)
        }
    }

    companion object {
        private const val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z"
    }
}
