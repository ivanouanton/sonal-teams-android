package com.waveneuro.domain.model.session

data class SessionRq(
    var eegId: Long? = null,
    var finishedAt: Long? = null,
    var isCompleted: Boolean = false,
    var patientId: Long? = null,
    var protocolId: Long? = null,
    var sonalId: String? = null,
)