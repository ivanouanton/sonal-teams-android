package com.waveneuro.ui.model.organization.mapper

import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.ui.model.organization.OrganizationUi
import javax.inject.Inject

class OrganizationMapperImpl @Inject constructor() : OrganizationMapper {

    override fun fromApiToUi(api: OrganizationResponse) = OrganizationUi(
        api.id,
        api.name
    )

}