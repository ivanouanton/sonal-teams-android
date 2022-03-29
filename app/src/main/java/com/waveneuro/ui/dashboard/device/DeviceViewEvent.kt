package com.waveneuro.ui.dashboard.device

import com.waveneuro.data.model.entity.BleDevice

sealed class DeviceViewEvent {
    class Start : DeviceViewEvent()
    class LocateDevice : DeviceViewEvent()
    class DeviceClicked(val bleDevice: BleDevice)  : DeviceViewEvent()
    class Connected : DeviceViewEvent()
    class Disconnected : DeviceViewEvent()
    class Searched : DeviceViewEvent()
    class NoDeviceFound : DeviceViewEvent()
    class BackClicked : DeviceViewEvent()
    class StartSessionClicked : DeviceViewEvent()
    class LocateDeviceNextClicked : DeviceViewEvent()
}