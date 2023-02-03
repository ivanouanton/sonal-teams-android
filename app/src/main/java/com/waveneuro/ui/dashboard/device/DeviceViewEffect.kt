package com.waveneuro.ui.dashboard.device

sealed class DeviceViewEffect {
    object BackRedirect : DeviceViewEffect()
    data class SessionRedirect(
        val treatmentLength: String,
        val protocolFrequency: String,
        val sonalId: String,
//        val client: String
    ) : DeviceViewEffect()
}