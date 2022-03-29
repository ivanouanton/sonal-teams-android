package com.waveneuro.ui.dashboard.device

import com.asif.abase.data.model.BaseModel
import com.waveneuro.data.model.entity.BleDevice
import com.waveneuro.data.model.entity.User

sealed class DeviceViewState {
    class Loading(val loading: Boolean) : DeviceViewState()
    class Success(val item: List<BaseModel>) : DeviceViewState()
    class Failure(val error: Error) : DeviceViewState()
    class InitLocateDevice(val user: User) : DeviceViewState()
    class LocateDevice : DeviceViewState()
    class LocateDeviceNext : DeviceViewState()
    class Searching : DeviceViewState()
    class Connecting(val bleDevice: BleDevice) : DeviceViewState()
    class Connected : DeviceViewState()
    class Searched : DeviceViewState()
}