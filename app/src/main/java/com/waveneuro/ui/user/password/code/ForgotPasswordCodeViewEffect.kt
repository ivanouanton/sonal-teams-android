package com.waveneuro.ui.user.password.code

sealed class ForgotPasswordCodeViewEffect {
    object Home : ForgotPasswordCodeViewEffect()
    object ForgotPassword : ForgotPasswordCodeViewEffect()
    object Register : ForgotPasswordCodeViewEffect()
    object Support : ForgotPasswordCodeViewEffect()
    data class RememberMe(val username: String) : ForgotPasswordCodeViewEffect()
    data class EnterMfaCode(val session: String) : ForgotPasswordCodeViewEffect()
    object WrongMfaCode : ForgotPasswordCodeViewEffect()
}