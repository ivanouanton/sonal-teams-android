package com.waveneuro.ui.user.password.reset

sealed class ResetPasswordViewEvent {
    data class ForgotPasswordClicked(val username: String) : ResetPasswordViewEvent()
    object BackClicked : ResetPasswordViewEvent()
    object RegisterClicked : ResetPasswordViewEvent()
    object LoginClicked : ResetPasswordViewEvent()
    object AboutUsClicked : ResetPasswordViewEvent()
    object ForgotUsernameClicked : ResetPasswordViewEvent()
}