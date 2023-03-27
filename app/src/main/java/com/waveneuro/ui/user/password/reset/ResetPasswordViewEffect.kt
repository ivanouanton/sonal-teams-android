package com.waveneuro.ui.user.password.reset

import com.waveneuro.ui.user.login.LoginViewEffect

sealed class ResetPasswordViewEffect {
    object BackRedirect : ResetPasswordViewEffect()
    object ForgotUsernameRedirect : ResetPasswordViewEffect()
    object RegisterRedirect : ResetPasswordViewEffect()
    object LoginRedirect : ResetPasswordViewEffect()
    object AboutUsRedirect : ResetPasswordViewEffect()
    object Success: ResetPasswordViewEffect()
    data class ShowErrorDialog(val title: String?, val message: String) : ResetPasswordViewEffect()

}