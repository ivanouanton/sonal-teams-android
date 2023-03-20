package com.waveneuro.ui.dashboard

import com.waveneuro.domain.model.ble.BleDevice

sealed class DashboardViewState {
    data class Connect(val data: BleDevice) : DashboardViewState()
    object Disconnect : DashboardViewState()
}