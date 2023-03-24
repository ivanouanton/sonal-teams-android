package com.waveneuro.ui.session.session

sealed class SessionViewEvent {
    object Initializing : SessionViewEvent()
    object BackClicked : SessionViewEvent()
    data class Start(val doNotShowAgain: Boolean = false) : SessionViewEvent()
    object InitializeDevice : SessionViewEvent()
    object LocatingDevice : SessionViewEvent()
    object StartSessionClicked : SessionViewEvent()
    object StartSession : SessionViewEvent()
    object DevicePaused : SessionViewEvent()
    object ResumeSession : SessionViewEvent()
    object EndSession : SessionViewEvent()
    data class DeviceError(val title: String, val message: String) : SessionViewEvent()
    object DeviceDisconnected : SessionViewEvent()
}