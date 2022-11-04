package com.waveneuro.ui.user.password.password.confirm

sealed class SetNewPasswordViewEvent {
    class Start : SetNewPasswordViewEvent()
    data class SetNewPassword(val email: String,
                              val code: String,
                              val password: String) : SetNewPasswordViewEvent()
}