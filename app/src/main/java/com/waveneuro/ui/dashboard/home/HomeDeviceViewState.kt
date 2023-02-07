package com.waveneuro.ui.dashboard.home

sealed class HomeDeviceViewState {
    object PairDevice : HomeDeviceViewState()
    object StartSession : HomeDeviceViewState()
}