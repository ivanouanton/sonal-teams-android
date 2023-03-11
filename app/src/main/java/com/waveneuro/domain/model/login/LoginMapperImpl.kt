package com.waveneuro.domain.model.login

import com.waveneuro.data.api.user.model.login.ApiLoginRsMfa
import javax.inject.Inject

class LoginMapperImpl @Inject constructor() : LoginMapper {

    override fun fromApiToDomain(api: ApiLoginRsMfa): LoginRsMfa = with(api) {
        LoginRsMfa(
            challengeName,
            session
        )
    }

}