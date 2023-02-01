package com.waveneuro.ui.session.session

sealed class SessionViewEffect {
    object Back : SessionViewEffect()
    object InitializeBle : SessionViewEffect()
}