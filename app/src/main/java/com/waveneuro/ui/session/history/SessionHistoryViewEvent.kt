package com.waveneuro.ui.session.history

sealed class SessionHistoryViewEvent {
    data class Start(val id: Int) : SessionHistoryViewEvent()
}