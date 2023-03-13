package com.waveneuro.domain.usecase.login

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRq
import com.waveneuro.domain.model.token.Token
import com.waveneuro.domain.model.token.TokenMapperImpl
import javax.inject.Inject

class ConfirmTokenUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: TokenMapperImpl
) {

    suspend fun confirmToken(username: String, mfaCode: String, session: String): Token =
        mapper.fromApiToDomain(userApi.confirmSoftwareToken(
            ApiConfirmTokenRq(username, mfaCode, session)))

}