package com.waveneuro.ui.session.session

sealed class SessionViewEffect {
    object Back : SessionViewEffect()
    object InitializeBle : SessionViewEffect()
    object ShowLoader : SessionViewEffect()
    object HideLoader : SessionViewEffect()
}