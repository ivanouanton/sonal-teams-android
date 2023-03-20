package com.waveneuro.domain.model.protocol

import com.waveneuro.data.api.user.model.protocol.ApiProtocolRs

interface ProtocolMapper {
    fun fromApiToDomain(api: ApiProtocolRs): ProtocolRs
}