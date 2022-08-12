package com.waveneuro.ui.dashboard

import com.waveneuro.data.model.entity.BleDevice

sealed class DashboardViewEvent {
    class Connected(val bleDevice: BleDevice) : DashboardViewEvent()
    class Disconnected : DashboardViewEvent()
    class AccountClicked : DashboardViewEvent()
    class DeviceClicked : DashboardViewEvent()
    class LogoutClicked : DashboardViewEvent()
    class HelpClicked : DashboardViewEvent()
    class DeviceHistoryClicked : DashboardViewEvent()
}