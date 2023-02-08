package com.waveneuro.ui.dashboard.organization.mapper

import com.waveneuro.data.model.response.user.Organization
import com.waveneuro.ui.dashboard.organization.adapter.model.OrganizationItem

interface OrganizationMapper {

    fun fromDomainToUi(domain: List<Organization>) : List<OrganizationItem>

}