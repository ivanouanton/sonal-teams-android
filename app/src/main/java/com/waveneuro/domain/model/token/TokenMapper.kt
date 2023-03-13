package com.waveneuro.domain.model.token

import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRs

interface TokenMapper {
    fun fromApiToDomain(api: ApiConfirmTokenRs): Token
}