package com.waveneuro.ui.dashboard

import com.waveneuro.domain.model.ble.BleDevice

sealed class DashboardViewEvent {
    data class Connected(val bleDevice: BleDevice) : DashboardViewEvent()
    object Disconnected : DashboardViewEvent()
    object AccountClicked : DashboardViewEvent()
    object DeviceClicked : DashboardViewEvent()
}