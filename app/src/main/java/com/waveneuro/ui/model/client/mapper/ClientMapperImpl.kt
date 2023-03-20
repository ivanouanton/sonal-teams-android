package com.waveneuro.ui.model.client.mapper

import com.waveneuro.domain.model.client.ClientRs
import com.waveneuro.ui.model.client.ClientUi
import javax.inject.Inject

class ClientMapperImpl @Inject constructor() : ClientMapper {

    override fun fromDomainToUi(domain: ClientRs) = with(domain) {
        ClientUi(
            id,
            firstName,
            lastName,
            birthday,
            email,
            username,
            isMale,
            organization,
            isTosSigned,
            tosStatus,
            alternativeIds,
            imageURLString,
        )
    }

    override fun fromUiToDomain(ui: ClientUi) = with(ui) {
        ClientRs(
            id,
            firstName,
            lastName,
            birthday,
            email,
            username,
            isMale,
            organization,
            isTosSigned,
            tosStatus,
            alternativeIds,
            imageURLString,
        )
    }

}