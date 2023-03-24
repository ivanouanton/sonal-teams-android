package com.waveneuro.domain.usecase.login

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.mfa.ApiConfirmTokenRq
import com.waveneuro.domain.model.mfa.ConfirmTokenRs
import com.waveneuro.domain.model.mfa.MfaMapperImpl
import javax.inject.Inject

class ConfirmTokenUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: MfaMapperImpl
) {

    suspend fun confirmToken(username: String, mfaCode: String, session: String): ConfirmTokenRs =
        mapper.fromApiToDomain(
            userApi.confirmSoftwareToken(ApiConfirmTokenRq(username, mfaCode, session))
        )

}