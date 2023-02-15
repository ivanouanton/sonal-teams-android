package com.waveneuro.ui.model.client.mapper

import com.waveneuro.data.model.response.client.ClientResponse
import com.waveneuro.ui.model.client.ClientUi

interface ClientMapper {

    fun fromApiToUi(api: ClientResponse) : ClientUi

    fun fromApiToUi(api: List<ClientResponse>) : List<ClientUi> =
        api.map(::fromApiToUi)

    fun fromUiToApi(ui: ClientUi) : ClientResponse

    fun fromUiToApi(ui: List<ClientUi>) : List<ClientResponse> =
        ui.map(::fromUiToApi)

}