package com.waveneuro.ui.dashboard.home.mapper

import com.waveneuro.data.model.response.patient.PatientResponse
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem

interface PatientMapper {

    fun fromDomainToUi(domain: PatientResponse) : PatientItem

    fun fromDomainToUi(domain: List<PatientResponse>) : List<PatientItem> =
        domain.map(::fromDomainToUi)

}