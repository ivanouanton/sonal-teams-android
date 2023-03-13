package com.waveneuro.domain.model.password

import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRs

interface PasswordMapper {
    fun fromApiToDomain(api: ApiSetNewPasswordRs): SetNewPasswordRs
}