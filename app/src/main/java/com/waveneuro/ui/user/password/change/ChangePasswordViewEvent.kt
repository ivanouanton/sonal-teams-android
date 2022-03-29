package com.waveneuro.ui.user.password.change

sealed class ChangePasswordViewEvent {
    class BackClicked : ChangePasswordViewEvent()
    class ForgotPasswordClicked : ChangePasswordViewEvent()
    class ContactUsClicked : ChangePasswordViewEvent()
    class Start : ChangePasswordViewEvent()
}