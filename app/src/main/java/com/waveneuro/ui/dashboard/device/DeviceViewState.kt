package com.waveneuro.ui.dashboard.device

import com.waveneuro.domain.model.ble.BleDevice
import com.waveneuro.domain.model.user.UserInfo

sealed class DeviceViewState {
    object Success : DeviceViewState()
    data class InitLocateDevice(val user: UserInfo) : DeviceViewState()
    object LocateDevice : DeviceViewState()
    object LocateDeviceNext : DeviceViewState()
    object Searching : DeviceViewState()
    data class Connecting(val bleDevice: BleDevice) : DeviceViewState()
    object Connected : DeviceViewState()
    object Searched : DeviceViewState()
}