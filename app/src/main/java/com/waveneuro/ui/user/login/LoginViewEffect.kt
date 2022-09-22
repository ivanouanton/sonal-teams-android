package com.waveneuro.ui.user.login

sealed class LoginViewEffect {
    class Home : LoginViewEffect()
    class ForgotUsername : LoginViewEffect()
    class ForgotPassword : LoginViewEffect()
    data class RememberMe(val username: String) : LoginViewEffect()
    class Register : LoginViewEffect()
    class Support : LoginViewEffect()
    class SetNewPassword : LoginViewEffect()
    data class EnterMfaCode(val session: String) : LoginViewEffect()
    class WrongMfaCode() : LoginViewEffect()
}