package com.waveneuro.ui.user.mfa

sealed class MfaViewEffect {
    object Home : MfaViewEffect()
    object WrongMfaCode : MfaViewEffect()
}