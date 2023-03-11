package com.waveneuro.ui.user.login

sealed class LoginViewEffect {
    object Home : LoginViewEffect()
    object ForgotPassword : LoginViewEffect()
    data class RememberMe(val username: String) : LoginViewEffect()
    object Register : LoginViewEffect()
    object Support : LoginViewEffect()
    object SetNewPassword : LoginViewEffect()
    data class EnterMfaCode(val session: String) : LoginViewEffect()
    object WrongMfaCode : LoginViewEffect()
    data class ShowErrorDialog(val title: String?, val message: String) : LoginViewEffect()
}