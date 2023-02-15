package com.waveneuro.ui.model.organization.mapper

import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.ui.model.organization.OrganizationUi

interface OrganizationMapper {

    fun fromApiToUi(api: OrganizationResponse) : OrganizationUi

    fun fromApiToUi(api: List<OrganizationResponse>): List<OrganizationUi> =
        api.map(::fromApiToUi)

}