package com.waveneuro.ui.dashboard.device

import com.asif.abase.data.model.BaseModel
import com.waveneuro.data.model.entity.BleDevice
import com.waveneuro.data.model.entity.User

sealed class DeviceViewState {
    data class Loading(val loading: Boolean) : DeviceViewState()
    data class Success(val item: List<BaseModel>) : DeviceViewState()
    data class Failure(val error: Error) : DeviceViewState()
    data class InitLocateDevice(val user: User) : DeviceViewState()
    object LocateDevice : DeviceViewState()
    object LocateDeviceNext : DeviceViewState()
    object Searching : DeviceViewState()
    data class Connecting(val bleDevice: BleDevice) : DeviceViewState()
    object Connected : DeviceViewState()
    object Searched : DeviceViewState()
}