package com.waveneuro.data.api.user.model.protocol

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiProtocolRs(
    @Json(name = "eeg_id") val eegId: String,
    @Json(name = "id") val id: String,
    @Json(name = "modified_at") val modifiedAt: String,
    @Json(name = "protocol_date_processed") val protocolDateProcessed: String,
    @Json(name = "protocol_frequency") val protocolFrequency: String,
    @Json(name = "status") val status: String,
    @Json(name = "treatment_length") val treatmentLength: String
)