package com.waveneuro.ui.dashboard.home

sealed class HomeViewEvent {
    class Start : HomeViewEvent()
    class PairDevice : HomeViewEvent()
    class DeviceConnected : HomeViewEvent()
    class DeviceDisconnected : HomeViewEvent()
    class StartSessionClicked : HomeViewEvent()
}