package com.waveneuro.ui.user.login

sealed class LoginViewEvent {
    data class LoginClicked(val username: String, val  password: String) : LoginViewEvent()
    object ForgotPasswordClicked : LoginViewEvent()
    data class RememberUser(val username: String) : LoginViewEvent()
    object ClearRememberUser : LoginViewEvent()
    object RegisterClicked : LoginViewEvent()
    object SupportClicked : LoginViewEvent()
    object Start : LoginViewEvent()
}