package com.waveneuro.ui.dashboard

sealed class DashboardViewEffect {
    object Login : DashboardViewEffect()
    object Account : DashboardViewEffect()
    object Help : DashboardViewEffect()
    object DeviceHistory : DashboardViewEffect()
    data class Device(val deviceName: String) : DashboardViewEffect()
}