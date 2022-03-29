package com.waveneuro.ui.dashboard.home

sealed class HomeDeviceViewState {
    class PairDevice : HomeDeviceViewState()
    class StartSession : HomeDeviceViewState()
}