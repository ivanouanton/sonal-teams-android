package com.waveneuro.domain.model.session

data class Session(
    val eegRecordedAt: Double,
    val finishedAt: Double,
    val isCompleted: Boolean,
    val sonalId: String
)