package com.waveneuro.domain.model.login

import com.waveneuro.data.api.user.model.login.ApiLoginMfaRs

interface LoginMapper {
    fun fromApiToDomain(api: ApiLoginMfaRs): LoginMfaRs
}