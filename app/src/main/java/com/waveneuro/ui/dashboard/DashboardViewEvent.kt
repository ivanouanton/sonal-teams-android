package com.waveneuro.ui.dashboard

import com.waveneuro.data.model.entity.BleDevice

sealed class DashboardViewEvent {
    data class Connected(val bleDevice: BleDevice) : DashboardViewEvent()
    object Disconnected : DashboardViewEvent()
    object AccountClicked : DashboardViewEvent()
    object DeviceClicked : DashboardViewEvent()
}