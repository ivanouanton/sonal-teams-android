package com.waveneuro.domain.model.login

import com.waveneuro.data.api.user.model.login.ApiLoginRsMfa

interface LoginMapper {
    fun fromApiToDomain(api: ApiLoginRsMfa): LoginRsMfa
}