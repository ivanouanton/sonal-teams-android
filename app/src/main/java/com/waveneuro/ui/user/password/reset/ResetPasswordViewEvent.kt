package com.waveneuro.ui.user.password.reset

sealed class ResetPasswordViewEvent {
    data class ForgotPasswordClicked(val username: String) : ResetPasswordViewEvent()
    class BackClicked : ResetPasswordViewEvent()
    class RegisterClicked : ResetPasswordViewEvent()
    class LoginClicked : ResetPasswordViewEvent()
    class AboutUsClicked : ResetPasswordViewEvent()
    class ForgotUsernameClicked : ResetPasswordViewEvent()
    class Start : ResetPasswordViewEvent()
}