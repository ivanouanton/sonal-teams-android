package com.waveneuro.domain.model.client

import com.waveneuro.data.api.user.model.user.ApiUserInfoRs
import com.waveneuro.data.api.user.model.user.ApiOrganizationRs
import javax.inject.Inject

class ClientMapperImpl @Inject constructor() : ClientMapper {

    override fun fromApiToDomain(api: ApiUserInfoRs): ClientInfo = with(api) {
        ClientInfo(
            id,
            firstName,
            lastName,
            email,
            role,
            organizations.map(::fromApiToDomain)
        )
    }

    private fun fromApiToDomain(api: ApiOrganizationRs): Organization = with(api) {
        Organization(
            id, name
        )
    }

}