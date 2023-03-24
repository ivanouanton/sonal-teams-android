package com.waveneuro.ui.user.mfa

sealed class MfaViewEvent {
    data class ConfirmToken(
        val mfaCode: String?,
        val username: String?,
        val session: String?
    ) : MfaViewEvent()
}