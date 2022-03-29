package com.waveneuro.ui.device

sealed class MyDeviceViewEvent {
    class Start : MyDeviceViewEvent()
    class BackClicked : MyDeviceViewEvent()
}