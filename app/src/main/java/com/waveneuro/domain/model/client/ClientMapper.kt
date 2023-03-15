package com.waveneuro.domain.model.client

import com.waveneuro.data.api.user.model.user.ApiUserInfoRs

interface ClientMapper {
    fun fromApiToDomain(api: ApiUserInfoRs): ClientInfo
}