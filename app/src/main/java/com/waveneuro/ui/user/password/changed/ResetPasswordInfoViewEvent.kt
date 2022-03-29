package com.waveneuro.ui.user.password.changed

sealed class ResetPasswordInfoViewEvent {
    class LoginClicked : ResetPasswordInfoViewEvent()
    class BackClicked : ResetPasswordInfoViewEvent()
    class Start : ResetPasswordInfoViewEvent()
}