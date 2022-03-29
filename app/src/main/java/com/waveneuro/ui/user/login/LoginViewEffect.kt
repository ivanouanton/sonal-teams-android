package com.waveneuro.ui.user.login

sealed class LoginViewEffect {
    class Home : LoginViewEffect()
    class ForgotUsername : LoginViewEffect()
    class ForgotPassword : LoginViewEffect()
    data class RememberMe(val username: String, val password: String) : LoginViewEffect()
    class Register : LoginViewEffect()
    class Support : LoginViewEffect()
    class SetNewPassword : LoginViewEffect()
}