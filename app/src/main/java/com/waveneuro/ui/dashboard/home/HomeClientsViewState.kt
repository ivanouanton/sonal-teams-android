package com.waveneuro.ui.dashboard.home

import com.waveneuro.domain.model.user.Organization
import com.waveneuro.ui.model.client.ClientUi

sealed class HomeClientsViewState {
    data class ClientsSuccess(val clientList: List<ClientUi>) : HomeClientsViewState()
    data class ClientProtocolSuccess(val client: ClientUi, val isTreatmentDataPresent: Boolean) : HomeClientsViewState()
    data class OrganizationsSuccess(val organizationList: List<Organization>) : HomeClientsViewState()
}