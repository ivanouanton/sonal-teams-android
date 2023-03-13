package com.waveneuro.domain.model.token

import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRs
import javax.inject.Inject

class TokenMapperImpl @Inject constructor() : TokenMapper {

    override fun fromApiToDomain(api: ApiConfirmTokenRs): Token = with(api) {
        Token(
            accessToken,
            refreshToken
        )
    }

}