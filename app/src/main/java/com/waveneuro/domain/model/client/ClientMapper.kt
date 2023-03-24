package com.waveneuro.domain.model.client

import com.waveneuro.data.api.user.model.client.ApiClientListRs
import com.waveneuro.data.api.user.model.client.ApiClientRq
import com.waveneuro.data.api.user.model.client.ApiClientRs

interface ClientMapper {
    fun fromApiToDomain(api: ApiClientListRs): ClientListRs
    fun fromApiToDomain(api: ApiClientRs): ClientRs
    fun fromDomainToApi(domain: ClientRq): ApiClientRq
}