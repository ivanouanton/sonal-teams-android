package com.waveneuro.ui.user.password.change

sealed class ChangePasswordViewEffect {
    class Back : ChangePasswordViewEffect()
    class ResetSuccess : ChangePasswordViewEffect()
    class ContactUs : ChangePasswordViewEffect()
}