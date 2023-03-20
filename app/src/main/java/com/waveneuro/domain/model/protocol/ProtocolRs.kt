package com.waveneuro.domain.model.protocol

data class ProtocolRs(
    val eegId: String,
    val id: String,
    val modifiedAt: String,
    val protocolDateProcessed: String,
    val protocolFrequency: String,
    val status: String,
    val treatmentLength: String
)