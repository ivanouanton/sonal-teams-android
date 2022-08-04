package com.waveneuro.ui.session.session

import com.asif.abase.data.model.BaseError
import com.waveneuro.data.model.entity.User

sealed class SessionViewState {
    class Loading(val loading: Boolean) : SessionViewState()
    class Success(val item: User) : SessionViewState()
    class Failure(val error: BaseError) : SessionViewState()
    class Initializing : SessionViewState()
    class LocateDevice : SessionViewState()
    class SessionStarted : SessionViewState()
    class ResumeSession : SessionViewState()
    class SessionEnded : SessionViewState()
    class SessionPaused : SessionViewState()
    class ErrorSession(val title: String, val message: String) : SessionViewState()
    class DeviceDisconnected : SessionViewState()
    class ErrorSending : SessionViewState()
}