package com.waveneuro.ui.dashboard.organization.mapper

import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.ui.dashboard.organization.adapter.model.OrganizationItem

interface OrganizationMapper {

    fun fromDomainToUi(domain: OrganizationResponse) : OrganizationItem

    fun fromDomainToUi(domain: List<OrganizationResponse>): List<OrganizationItem> =
        domain.map(::fromDomainToUi)

}