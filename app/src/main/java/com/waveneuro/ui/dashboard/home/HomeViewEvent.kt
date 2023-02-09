package com.waveneuro.ui.dashboard.home

sealed class HomeViewEvent {
    object Start : HomeViewEvent()
    data class NewQuery(val query: String) : HomeViewEvent()
    object PairDevice : HomeViewEvent()
    object DeviceConnected : HomeViewEvent()
    object DeviceDisconnected : HomeViewEvent()
    object StartSessionClicked : HomeViewEvent()
    object SonalWebsiteClicked : HomeViewEvent()
}