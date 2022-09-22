package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.entity.User
import com.waveneuro.data.model.response.patient.PatientResponse

sealed class HomeClientsViewState {
    class Success(val item: PatientResponse) : HomeClientsViewState()
}