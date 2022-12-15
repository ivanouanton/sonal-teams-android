package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.response.patient.PatientListResponse
import com.waveneuro.data.model.response.patient.PatientResponse

sealed class HomeClientsViewState {
    class Success(val patientList: List<PatientListResponse.Patient>) : HomeClientsViewState()
    class OrganizationSuccess(val item: List<PatientListResponse.Patient.Organization>) : HomeClientsViewState()
    class PatientSuccess(val item: PatientResponse, val treatmentDataPresent: Boolean) : HomeClientsViewState()
    class PatientSessionSuccess(val id: Int) : HomeClientsViewState()
}