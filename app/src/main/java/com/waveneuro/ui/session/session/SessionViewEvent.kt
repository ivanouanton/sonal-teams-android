package com.waveneuro.ui.session.session

sealed class SessionViewEvent {
    class BackClicked : SessionViewEvent()
    class Start : SessionViewEvent()
    class InitializeDevice : SessionViewEvent()
    class LocatingDevice : SessionViewEvent()
    class StartSession : SessionViewEvent()
    class DevicePaused : SessionViewEvent()
    class ResumeSession : SessionViewEvent()
    class EndSession : SessionViewEvent()
    class DeviceError(val title: String, val message: String) : SessionViewEvent()
    class DeviceDisconnected : SessionViewEvent()
}