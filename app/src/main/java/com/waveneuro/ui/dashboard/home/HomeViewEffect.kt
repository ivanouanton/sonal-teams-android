package com.waveneuro.ui.dashboard.home

sealed class HomeViewEffect {
    object BackRedirect : HomeViewEffect()
//    object DeviceRedirect : HomeViewEffect()
    data class SessionRedirect(
        val treatmentLength: String,
        val protocolFrequency: String,
        val sonalId: String
    ) : HomeViewEffect()
}