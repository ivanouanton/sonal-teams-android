package com.waveneuro.domain.model.mfa

data class MfaTokens(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String,
)
