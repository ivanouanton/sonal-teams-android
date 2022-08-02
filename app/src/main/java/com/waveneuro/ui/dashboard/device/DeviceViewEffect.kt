package com.waveneuro.ui.dashboard.device

sealed class DeviceViewEffect {
    class BackRedirect : DeviceViewEffect()
    class SessionRedirect(val treatmentLength: String, val protocolFrequency: String, val sonalId: String) :
        DeviceViewEffect()
}