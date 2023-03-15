package com.waveneuro.data.api.user.model.session

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSessionRq(
    @Json(name = "eeg_id") val eegId: Long,
    @Json(name = "finished_at") val finishedAt: Long,
    @Json(name = "is_completed") val isCompleted: Boolean,
    @Json(name = "patient_id") val patientId: Long,
    @Json(name = "protocol_id") val protocolId: Long,
    @Json(name = "sonal_id") val sonalId: String,
)