package com.waveneuro.ui.model.organization.mapper

import com.waveneuro.domain.model.client.Organization
import com.waveneuro.ui.model.organization.UiOrganization
import javax.inject.Inject

class OrganizationMapperImpl @Inject constructor() : OrganizationMapper {

    override fun fromDomainToUi(domain: Organization) = UiOrganization(
        domain.id,
        domain.name
    )

}