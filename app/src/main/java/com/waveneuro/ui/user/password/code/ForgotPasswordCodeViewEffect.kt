package com.waveneuro.ui.user.password.code

sealed class ForgotPasswordCodeViewEffect {
    object Home : ForgotPasswordCodeViewEffect()
    data class RememberMe(val username: String) : ForgotPasswordCodeViewEffect()
    data class EnterMfaCode(val session: String) : ForgotPasswordCodeViewEffect()
    object WrongMfaCode : ForgotPasswordCodeViewEffect()
}