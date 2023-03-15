package com.waveneuro.ui.dashboard.device

import com.waveneuro.domain.model.ble.BleDevice

sealed class DeviceViewEvent {
    object Start : DeviceViewEvent()
    object LocateDevice : DeviceViewEvent()
    data class DeviceClicked(val bleDevice: BleDevice) : DeviceViewEvent()
    data class SetDeviceId(val deviceId: String) : DeviceViewEvent()
    object Connected : DeviceViewEvent()
    object Disconnected : DeviceViewEvent()
    object Searched : DeviceViewEvent()
    object NoDeviceFound : DeviceViewEvent()
    object StartSessionClicked : DeviceViewEvent()
    object LocateDeviceNextClicked : DeviceViewEvent()
}