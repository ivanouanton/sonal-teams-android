package com.waveneuro.ui.session.complete

sealed class SessionCompleteViewEffect {
    class Back : SessionCompleteViewEffect()
    class Home : SessionCompleteViewEffect()
}