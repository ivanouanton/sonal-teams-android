package com.waveneuro.ui.dashboard.organization.mapper

import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.ui.dashboard.organization.adapter.model.OrganizationItem
import javax.inject.Inject

class OrganizationMapperImpl @Inject constructor() : OrganizationMapper {

    override fun fromDomainToUi(domain: OrganizationResponse) = OrganizationItem(
        domain.id,
        domain.name
    )

}