package com.waveneuro.ui.session.complete

sealed class SessionCompleteViewEvent {
    class BackClicked : SessionCompleteViewEvent()
    class Start : SessionCompleteViewEvent()
    class HomeClicked : SessionCompleteViewEvent()
}