package com.waveneuro.ui.session.session

sealed class SessionViewState {
    object Initializing : SessionViewState()
    object LocateDevice : SessionViewState()
    data class StartSessionClicked(val isPrecautionsShowed: Boolean) : SessionViewState()
    object SessionStarted : SessionViewState()
    object ResumeSession : SessionViewState()
    object SessionEnded : SessionViewState()
    object SessionPaused : SessionViewState()
    data class ErrorSession(val title: String, val message: String) : SessionViewState()
    object DeviceDisconnected : SessionViewState()
    object ErrorSending : SessionViewState()
}