package com.waveneuro.domain.model.login

import com.waveneuro.data.api.user.model.login.ApiLoginMfaRs
import javax.inject.Inject

class LoginMapperImpl @Inject constructor() : LoginMapper {

    override fun fromApiToDomain(api: ApiLoginMfaRs): LoginMfaRs = with(api) {
        LoginMfaRs(
            challengeName,
            session
        )
    }

}