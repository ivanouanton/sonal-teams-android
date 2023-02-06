package com.waveneuro.ui.dashboard.organization.mapper

import com.waveneuro.data.model.response.user.Organization
import com.waveneuro.ui.dashboard.organization.adapter.model.OrganizationItem
import javax.inject.Inject

class OrganizationMapperImpl @Inject constructor() : OrganizationMapper {

    override fun fromDomainToUi(domain: List<Organization>) : List<OrganizationItem> {
        return domain.map(::fromDomainToUiOrganization)
    }

    private fun fromDomainToUiOrganization(domain: Organization) = OrganizationItem(
        domain.id,
        domain.name
    )

}