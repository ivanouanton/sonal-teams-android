package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.response.patient.PatientResponse
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem

sealed class HomeClientsViewState {
    data class Success(val patientList: List<PatientItem>) : HomeClientsViewState()
    data class Error(val message: String?) : HomeClientsViewState()
    data class PatientSuccess(val item: PatientResponse, val treatmentDataPresent: Boolean) : HomeClientsViewState()
    data class PatientSessionSuccess(val id: Int) : HomeClientsViewState()
}