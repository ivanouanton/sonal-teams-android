package com.waveneuro.domain.model.login

data class LoginMfaRs(
    val challengeName: String?,
    val session: String,
)
