package com.waveneuro.domain.model.device

import java.text.SimpleDateFormat
import java.util.*

data class Device(
    val lastSessionAt: Long,
    val deviceName: String,
) {
    val lastConnection: String
        get() {
            val date = Date(lastSessionAt * 1000L)
            return "Last connection: " + SimpleDateFormat("MMM d, yyyy").format(date)
        }
}