package com.waveneuro.ui.user.password.new_password

sealed class SetNewPasswordViewEffect {
    object Home : SetNewPasswordViewEffect()
    object Success : SetNewPasswordViewEffect()
}