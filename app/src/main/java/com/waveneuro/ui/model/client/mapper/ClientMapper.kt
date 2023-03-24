package com.waveneuro.ui.model.client.mapper

import com.waveneuro.domain.model.client.ClientRs
import com.waveneuro.ui.model.client.ClientUi

interface ClientMapper {

    fun fromDomainToUi(domain: ClientRs) : ClientUi

    fun fromDomainToUi(domain: List<ClientRs>) : List<ClientUi> =
        domain.map(::fromDomainToUi)

    fun fromUiToDomain(ui: ClientUi) : ClientRs

    fun fromUiToDomain(ui: List<ClientUi>) : List<ClientRs> =
        ui.map(::fromUiToDomain)

}