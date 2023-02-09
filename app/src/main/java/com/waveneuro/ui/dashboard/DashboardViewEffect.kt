package com.waveneuro.ui.dashboard

sealed class DashboardViewEffect {
    object Account : DashboardViewEffect()
    data class Device(val deviceName: String) : DashboardViewEffect()
}