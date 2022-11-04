package com.waveneuro.ui.dashboard.home

sealed class HomeViewEvent {
    class Start(val startsWith: String, val filters: Array<Int>?) : HomeViewEvent()
    class PairDevice : HomeViewEvent()
    class DeviceConnected : HomeViewEvent()
    class DeviceDisconnected : HomeViewEvent()
    class StartSessionClicked : HomeViewEvent()
    class SonalWebsiteClicked : HomeViewEvent()
}