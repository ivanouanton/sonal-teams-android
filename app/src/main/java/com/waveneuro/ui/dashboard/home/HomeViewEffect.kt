package com.waveneuro.ui.dashboard.home

sealed class HomeViewEffect {
    class BackRedirect : HomeViewEffect()
    class DeviceRedirect : HomeViewEffect()
    class SessionRedirect(val treatmentLength: String, val protocolFrequency: String) :
        HomeViewEffect()
}