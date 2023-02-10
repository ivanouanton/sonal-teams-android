package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.ui.model.client.ClientUi

sealed class HomeClientsViewState {
    data class ClientsSuccess(val clientList: List<ClientUi>) : HomeClientsViewState()
    data class ClientProtocolSuccess(val client: ClientUi, val isTreatmentDataPresent: Boolean) : HomeClientsViewState()
    data class OrganizationsSuccess(val organizationList: List<OrganizationResponse>) : HomeClientsViewState()

    data class Error(val message: String?) : HomeClientsViewState()

    data class PatientSessionSuccess(val id: Int) : HomeClientsViewState()
}