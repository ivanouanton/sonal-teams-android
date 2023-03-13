package com.waveneuro.ui.dashboard.organization

import com.waveneuro.ui.model.organization.UiOrganization

sealed class OrganizationViewEffect {
    object BackRedirect : OrganizationViewEffect()
    data class Success(val organizations: List<UiOrganization> ) : OrganizationViewEffect()
}