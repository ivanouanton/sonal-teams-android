package com.waveneuro.ui.session.session

import com.asif.abase.data.model.BaseError
import com.waveneuro.data.model.entity.User

sealed class SessionViewState {
    data class Loading(val loading: Boolean) : SessionViewState()
    data class Success(val item: User) : SessionViewState()
    data class Failure(val error: BaseError) : SessionViewState()
    object Initializing : SessionViewState()
    object LocateDevice : SessionViewState()
    object SessionStarted : SessionViewState()
    object ResumeSession : SessionViewState()
    object SessionEnded : SessionViewState()
    object SessionPaused : SessionViewState()
    data class ErrorSession(val title: String, val message: String) : SessionViewState()
    object DeviceDisconnected : SessionViewState()
    object ErrorSending : SessionViewState()
}