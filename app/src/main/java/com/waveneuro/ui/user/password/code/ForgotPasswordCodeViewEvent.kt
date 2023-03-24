package com.waveneuro.ui.user.password.code

sealed class ForgotPasswordCodeViewEvent {
    object Start : ForgotPasswordCodeViewEvent()
    data class LoginClicked(val username: String, val  password: String) : ForgotPasswordCodeViewEvent()
    data class ResetPassword(val username: String?) : ForgotPasswordCodeViewEvent()
    data class RememberUser(val username: String) : ForgotPasswordCodeViewEvent()
    object ClearRememberUser : ForgotPasswordCodeViewEvent()
}