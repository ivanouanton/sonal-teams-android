package com.waveneuro.domain.model.mfa

import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRs

interface MfaMapper {
    fun fromApiToDomain(api: ApiConfirmTokenRs): ConfirmTokenRs
}