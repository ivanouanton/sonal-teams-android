package com.waveneuro.ui.session.session

sealed class SessionViewEvent {
    object Initializing : SessionViewEvent()
    object BackClicked : SessionViewEvent()
    object Start : SessionViewEvent()
    object InitializeDevice : SessionViewEvent()
    object LocatingDevice : SessionViewEvent()
    object StartSession : SessionViewEvent()
    object DevicePaused : SessionViewEvent()
    object ResumeSession : SessionViewEvent()
    object EndSession : SessionViewEvent()
    data class DeviceError(val title: String, val message: String) : SessionViewEvent()
    object DeviceDisconnected : SessionViewEvent()
}