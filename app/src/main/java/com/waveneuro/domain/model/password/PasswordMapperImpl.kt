package com.waveneuro.domain.model.password

import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRs
import javax.inject.Inject

class PasswordMapperImpl @Inject constructor() : PasswordMapper {

    override fun fromApiToDomain(api: ApiSetNewPasswordRs): SetNewPasswordRs = with(api) {
        SetNewPasswordRs(
            accessToken,
            refreshToken
        )
    }

}