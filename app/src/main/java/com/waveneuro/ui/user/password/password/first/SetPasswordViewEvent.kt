package com.waveneuro.ui.user.password.password.first

sealed class SetPasswordViewEvent {
    class Start : SetPasswordViewEvent()
    class LoginClicked : SetPasswordViewEvent()
    class RegisterClicked : SetPasswordViewEvent()
    data class SetPassword(val code: String,
                              val username: String,
                              val newPassword: String) : SetPasswordViewEvent()
}