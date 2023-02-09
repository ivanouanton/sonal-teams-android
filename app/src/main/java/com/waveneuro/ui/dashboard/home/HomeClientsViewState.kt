package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.data.model.response.patient.PatientResponse
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem

sealed class HomeClientsViewState {
    data class SuccessClients(val patientList: List<PatientItem>) : HomeClientsViewState()
    data class Error(val message: String?) : HomeClientsViewState()
    data class SuccessOrganizations(val organizationList: List<OrganizationResponse>) : HomeClientsViewState()

    data class PatientSuccess(val item: PatientResponse, val treatmentDataPresent: Boolean) : HomeClientsViewState()
    data class PatientSessionSuccess(val id: Int) : HomeClientsViewState()
}