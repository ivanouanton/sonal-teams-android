package com.waveneuro.ui.dashboard.device

import com.waveneuro.data.model.entity.BleDevice

sealed class DeviceViewEvent {
    object Start : DeviceViewEvent()
    object LocateDevice : DeviceViewEvent()
    data class DeviceClicked(val bleDevice: BleDevice)  : DeviceViewEvent()
    object Connected : DeviceViewEvent()
    object Disconnected : DeviceViewEvent()
    object Searched : DeviceViewEvent()
    object NoDeviceFound : DeviceViewEvent()
    object BackClicked : DeviceViewEvent()
    object StartSessionClicked : DeviceViewEvent()
    object LocateDeviceNextClicked : DeviceViewEvent()
}