package com.waveneuro.ui.dashboard

sealed class DashboardViewState {
    data class Connect(val data: BleDevice) : DashboardViewState()
    object Disconnect : DashboardViewState()
}