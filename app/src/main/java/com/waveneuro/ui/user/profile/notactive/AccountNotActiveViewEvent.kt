package com.waveneuro.ui.user.profile.notactive

sealed class AccountNotActiveViewEvent {
    data class LoginClicked(val email: String, val  password: String) : AccountNotActiveViewEvent()
    class BackClicked : AccountNotActiveViewEvent()
    class ForgotPasswordClicked : AccountNotActiveViewEvent()
    class Start : AccountNotActiveViewEvent()
}