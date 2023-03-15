package com.waveneuro.ui.model.organization.mapper

import com.waveneuro.domain.model.client.Organization
import com.waveneuro.ui.model.organization.UiOrganization

interface OrganizationMapper {

    fun fromDomainToUi(domain: Organization) : UiOrganization

    fun fromDomainToUi(domain: List<Organization>): List<UiOrganization> =
        domain.map(::fromDomainToUi)

}