package com.waveneuro.ui.user.email.forgot

sealed class ForgotUsernameViewEvent {
    class Start : ForgotUsernameViewEvent()
    data class ForgotUsernameClicked(val username: String) : ForgotUsernameViewEvent()
    class ForgotPasswordClicked : ForgotUsernameViewEvent()
    class RegisterClicked : ForgotUsernameViewEvent()
    class AboutUsClicked : ForgotUsernameViewEvent()
    class BackClicked : ForgotUsernameViewEvent()
}