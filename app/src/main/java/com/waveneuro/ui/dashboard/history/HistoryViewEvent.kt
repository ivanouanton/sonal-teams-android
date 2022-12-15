package com.waveneuro.ui.dashboard.history

sealed class HistoryViewEvent {
    class Start : HistoryViewEvent()
}