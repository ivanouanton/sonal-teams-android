package com.waveneuro.ui.user.password.password.first

sealed class SetPasswordViewEffect {
    class Back : SetPasswordViewEffect()
    class SignUpRedirect : SetPasswordViewEffect()
    class SignInRedirect : SetPasswordViewEffect()
}