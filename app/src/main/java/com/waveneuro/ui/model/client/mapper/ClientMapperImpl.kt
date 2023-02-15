package com.waveneuro.ui.model.client.mapper

import com.waveneuro.data.model.response.client.ClientResponse
import com.waveneuro.ui.model.client.ClientUi
import javax.inject.Inject

class ClientMapperImpl @Inject constructor() : ClientMapper {

    override fun fromApiToUi(api: ClientResponse) = with(api) {
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

    override fun fromUiToApi(ui: ClientUi) = with(ui) {
        ClientResponse(
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