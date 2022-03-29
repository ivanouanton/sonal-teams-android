package com.waveneuro.ui.dashboard

import com.waveneuro.data.model.entity.BleDevice

sealed class DashboardViewState {
    class Connect(val data: BleDevice) : DashboardViewState()
    class Disconnect: DashboardViewState()
}