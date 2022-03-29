package com.waveneuro.ui.user.password.reset

sealed class ResetPasswordViewEffect {
    class BackRedirect : ResetPasswordViewEffect()
    class ForgotUsernameRedirect : ResetPasswordViewEffect()
    class ResetPasswordRedirect : ResetPasswordViewEffect()
    class RegisterRedirect : ResetPasswordViewEffect()
    class LoginRedirect : ResetPasswordViewEffect()
    class AboutUsRedirect : ResetPasswordViewEffect()
}