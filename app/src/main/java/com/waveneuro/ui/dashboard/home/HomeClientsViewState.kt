package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.response.patient.PatientListResponse
import com.waveneuro.data.model.response.patient.PatientResponse

sealed class HomeClientsViewState {
    class Success(val item: PatientListResponse) : HomeClientsViewState()
    class OrganizationSuccess(val item: List<PatientListResponse.Patient.Organization>) : HomeClientsViewState()
    class PatientSuccess(val item: PatientResponse) : HomeClientsViewState()
}