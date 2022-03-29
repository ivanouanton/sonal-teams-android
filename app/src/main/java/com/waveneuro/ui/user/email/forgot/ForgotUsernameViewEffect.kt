package com.waveneuro.ui.user.email.forgot

sealed class ForgotUsernameViewEffect {
    class BackRedirect : ForgotUsernameViewEffect()
    class ForgotPasswordRedirect : ForgotUsernameViewEffect()
    class RegisterRedirect : ForgotUsernameViewEffect()
    class AboutUsRedirect : ForgotUsernameViewEffect()
}