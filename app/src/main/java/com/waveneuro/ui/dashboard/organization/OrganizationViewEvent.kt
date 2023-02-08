package com.waveneuro.ui.dashboard.organization

sealed class OrganizationViewEvent {
    object Start : OrganizationViewEvent()
    object BackClicked : OrganizationViewEvent()
}