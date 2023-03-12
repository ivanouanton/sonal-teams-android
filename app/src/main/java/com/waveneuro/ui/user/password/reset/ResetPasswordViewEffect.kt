package com.waveneuro.ui.user.password.reset

sealed class ResetPasswordViewEffect {
    object BackRedirect : ResetPasswordViewEffect()
    object ForgotUsernameRedirect : ResetPasswordViewEffect()
    object ResetPasswordRedirect : ResetPasswordViewEffect()
    object RegisterRedirect : ResetPasswordViewEffect()
    object LoginRedirect : ResetPasswordViewEffect()
    object AboutUsRedirect : ResetPasswordViewEffect()
    object Success: ResetPasswordViewEffect()
}