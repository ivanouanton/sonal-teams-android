package com.waveneuro.ui.dashboard.home

sealed class HomeViewEvent {
    class Start(val page: Integer, val startsWith: String, val filters: Array<Int>?) : HomeViewEvent()
    object PairDevice : HomeViewEvent()
    object DeviceConnected : HomeViewEvent()
    object DeviceDisconnected : HomeViewEvent()
    object StartSessionClicked : HomeViewEvent()
    object SonalWebsiteClicked : HomeViewEvent()
}