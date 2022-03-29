package com.waveneuro.ui.session.session

sealed class SessionViewEffect {
    class Back : SessionViewEffect()
    class InitializeBle : SessionViewEffect()
}