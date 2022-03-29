package com.waveneuro.ui.user.password.password.confirm

sealed class SetNewPasswordViewEvent {
    class Start : SetNewPasswordViewEvent()
    data class SetNewPassword(val username: String,
                              val oldPassword: String,
                              val newPassword: String) : SetNewPasswordViewEvent()
}