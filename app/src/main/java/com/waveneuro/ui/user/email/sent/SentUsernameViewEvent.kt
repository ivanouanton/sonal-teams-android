package com.waveneuro.ui.user.email.sent

sealed class SentUsernameViewEvent {
    data class LoginClicked(val email: String, val  password: String) : SentUsernameViewEvent()
    class BackClicked : SentUsernameViewEvent()
    class ForgotPasswordClicked : SentUsernameViewEvent()
    class Start : SentUsernameViewEvent()
}