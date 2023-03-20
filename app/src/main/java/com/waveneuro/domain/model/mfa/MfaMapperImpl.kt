package com.waveneuro.domain.model.mfa

import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRs
import com.waveneuro.data.api.user.model.mfa.ApiMfaTokens
import javax.inject.Inject

class MfaMapperImpl @Inject constructor() : MfaMapper {

    override fun fromApiToDomain(api: ApiConfirmTokenRs): ConfirmTokenRs = with(api) {
        ConfirmTokenRs(
            fromApiToDomain(tokens)
        )
    }

    private fun fromApiToDomain(api: ApiMfaTokens): MfaTokens = with(api) {
        MfaTokens(
            accessToken, idToken, refreshToken
        )
    }

}