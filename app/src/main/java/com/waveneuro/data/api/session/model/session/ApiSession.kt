package com.waveneuro.data.api.session.model.session

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSession(
    @Json(name = "eeg_recorded_at") val eegRecordedAt: Double,
    @Json(name = "finished_at") val finishedAt: Double,
    @Json(name = "is_completed") val isCompleted: Boolean,
    @Json(name = "sonal_id") val sonalId: String
)