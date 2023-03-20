package com.waveneuro.domain.model.protocol

import com.waveneuro.data.api.user.model.protocol.ApiProtocolRs
import javax.inject.Inject

class ProtocolMapperImpl @Inject constructor() : ProtocolMapper {

    override fun fromApiToDomain(api: ApiProtocolRs): ProtocolRs = with(api) {
        ProtocolRs(
            eegId,
            id,
            modifiedAt,
            protocolDateProcessed,
            protocolFrequency,
            status,
            treatmentLength
        )
    }

}