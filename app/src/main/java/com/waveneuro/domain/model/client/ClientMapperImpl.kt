package com.waveneuro.domain.model.client

import com.waveneuro.data.api.user.model.client.ApiAlternativeId
import com.waveneuro.data.api.user.model.client.ApiClientListRs
import com.waveneuro.data.api.user.model.client.ApiClientRq
import com.waveneuro.data.api.user.model.client.ApiClientRs
import com.waveneuro.data.api.user.model.user.ApiOrganizationRs
import com.waveneuro.domain.model.user.Organization
import javax.inject.Inject

class ClientMapperImpl @Inject constructor() : ClientMapper {

    override fun fromApiToDomain(api: ApiClientListRs): ClientListRs = with(api) {
        ClientListRs(
            hasNext,
            hasPrev,
            nextNum,
            page,
            pages,
            patients.map(::fromApiToDomain),
            total
        )
    }

    override fun fromApiToDomain(api: ApiClientRs): ClientRs = with(api) {
        ClientRs(
            id,
            firstName,
            lastName,
            birthday,
            email,
            username,
            isMale,
            fromApiToDomain(organization),
            isTosSigned,
            tosStatus,
            alternativeIds?.map(::fromApiToDomain),
            imageURLString
        )
    }

    override fun fromDomainToApi(domain: ClientRq): ApiClientRq = with(domain) {
        ApiClientRq(
            firstName, lastName, birthday, email, sex
        )
    }

    private fun fromApiToDomain(api: ApiAlternativeId): AlternativeId = with(api) {
        AlternativeId(
            id,
            name
        )
    }

    private fun fromApiToDomain(api: ApiOrganizationRs): Organization = with(api) {
        Organization(
            id, name
        )
    }

}