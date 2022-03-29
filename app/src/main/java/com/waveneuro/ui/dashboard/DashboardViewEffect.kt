package com.waveneuro.ui.dashboard

sealed class DashboardViewEffect {
    class Login : DashboardViewEffect()
    class Account : DashboardViewEffect()
    class Device(val deviceName: String) : DashboardViewEffect()
}