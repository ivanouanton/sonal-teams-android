package com.waveneuro.ui.user.password.new_password

sealed class SetNewPasswordViewEvent {
    data class SetNewPassword(val email: String,
                              val code: String,
                              val password: String) : SetNewPasswordViewEvent()
}