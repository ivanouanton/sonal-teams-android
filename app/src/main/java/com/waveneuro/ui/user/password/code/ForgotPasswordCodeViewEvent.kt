package com.waveneuro.ui.user.password.code

sealed class ForgotPasswordCodeViewEvent {
    object Start : ForgotPasswordCodeViewEvent()
    data class LoginClicked(val username: String, val  password: String) : ForgotPasswordCodeViewEvent()
    object ForgotPasswordClicked : ForgotPasswordCodeViewEvent()
    data class RememberUser(val username: String) : ForgotPasswordCodeViewEvent()
    object ClearRememberUser : ForgotPasswordCodeViewEvent()
    object RegisterClicked : ForgotPasswordCodeViewEvent()
    object SupportClicked : ForgotPasswordCodeViewEvent()
    data class SetNewPassword(val email: String,
                              val code: String,
                              val password: String) : ForgotPasswordCodeViewEvent()
}