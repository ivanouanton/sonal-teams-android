package com.waveneuro.ui.dashboard

import com.waveneuro.data.model.entity.BleDevice

sealed class DashboardViewState {
    data class Connect(val data: BleDevice) : DashboardViewState()
    object Disconnect : DashboardViewState()
}