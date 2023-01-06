package com.waveneuro.ui.user.login

sealed class LoginViewEvent {
    data class LoginClicked(val username: String, val  password: String) : LoginViewEvent()
    class ForgotPasswordClicked : LoginViewEvent()
    data class RememberUser(val username: String) : LoginViewEvent()
    class ClearRememberUser : LoginViewEvent()
    class RegisterClicked : LoginViewEvent()
    class SupportClicked : LoginViewEvent()
    class Start : LoginViewEvent()
}